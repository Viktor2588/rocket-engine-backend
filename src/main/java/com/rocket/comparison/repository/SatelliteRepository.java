package com.rocket.comparison.repository;

import com.rocket.comparison.entity.OrbitType;
import com.rocket.comparison.entity.Satellite;
import com.rocket.comparison.entity.SatelliteStatus;
import com.rocket.comparison.entity.SatelliteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    // ==================== By Identifier ====================

    Optional<Satellite> findByNoradId(String noradId);

    Optional<Satellite> findByCosparId(String cosparId);

    // ==================== By Country ====================

    List<Satellite> findByCountryIdOrderByLaunchDateDesc(Long countryId);

    List<Satellite> findByCountryIsoCodeOrderByLaunchDateDesc(String isoCode);

    @Query("SELECT s FROM Satellite s WHERE s.country.id = :countryId AND s.status = :status ORDER BY s.launchDate DESC")
    List<Satellite> findByCountryAndStatus(@Param("countryId") Long countryId, @Param("status") SatelliteStatus status);

    // ==================== By Status ====================

    List<Satellite> findByStatusOrderByLaunchDateDesc(SatelliteStatus status);

    @Query("SELECT s FROM Satellite s WHERE s.status = 'OPERATIONAL' OR s.status = 'PARTIALLY_OPERATIONAL' ORDER BY s.launchDate DESC")
    List<Satellite> findActiveSatellites();

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.status = 'OPERATIONAL' OR s.status = 'PARTIALLY_OPERATIONAL'")
    Long countActiveSatellites();

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.country.id = :countryId AND (s.status = 'OPERATIONAL' OR s.status = 'PARTIALLY_OPERATIONAL')")
    Long countActiveSatellitesByCountry(@Param("countryId") Long countryId);

    // ==================== By Type ====================

    List<Satellite> findBySatelliteTypeOrderByLaunchDateDesc(SatelliteType type);

    @Query("SELECT s FROM Satellite s WHERE s.satelliteType IN :types ORDER BY s.launchDate DESC")
    List<Satellite> findByTypes(@Param("types") List<SatelliteType> types);

    // ==================== By Orbit ====================

    List<Satellite> findByOrbitTypeOrderByLaunchDateDesc(OrbitType orbitType);

    @Query("SELECT s FROM Satellite s WHERE s.orbitType IN :orbits ORDER BY s.launchDate DESC")
    List<Satellite> findByOrbitTypes(@Param("orbits") List<OrbitType> orbitTypes);

    @Query("SELECT s FROM Satellite s WHERE s.altitudeKm BETWEEN :minAlt AND :maxAlt ORDER BY s.altitudeKm ASC")
    List<Satellite> findByAltitudeRange(@Param("minAlt") Double minAltitude, @Param("maxAlt") Double maxAltitude);

    // ==================== By Constellation ====================

    List<Satellite> findByConstellationOrderByLaunchDateDesc(String constellation);

    @Query("SELECT s FROM Satellite s WHERE s.isPartOfConstellation = true ORDER BY s.constellation, s.launchDate DESC")
    List<Satellite> findConstellationSatellites();

    @Query("SELECT DISTINCT s.constellation FROM Satellite s WHERE s.constellation IS NOT NULL ORDER BY s.constellation ASC")
    List<String> findAllConstellations();

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.constellation = :constellation")
    Long countByConstellation(@Param("constellation") String constellation);

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.constellation = :constellation AND (s.status = 'OPERATIONAL' OR s.status = 'PARTIALLY_OPERATIONAL')")
    Long countActiveByConstellation(@Param("constellation") String constellation);

    // ==================== By Operator ====================

    List<Satellite> findByOperatorOrderByLaunchDateDesc(String operator);

    @Query("SELECT DISTINCT s.operator FROM Satellite s WHERE s.operator IS NOT NULL ORDER BY s.operator ASC")
    List<String> findAllOperators();

    // ==================== Timeline ====================

    @Query("SELECT s FROM Satellite s WHERE s.launchYear = :year ORDER BY s.launchDate ASC")
    List<Satellite> findByLaunchYear(@Param("year") Integer year);

    @Query("SELECT s FROM Satellite s WHERE s.launchYear BETWEEN :startYear AND :endYear ORDER BY s.launchDate ASC")
    List<Satellite> findByLaunchYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    @Query("SELECT DISTINCT s.launchYear FROM Satellite s WHERE s.launchYear IS NOT NULL ORDER BY s.launchYear ASC")
    List<Integer> findAllLaunchYears();

    // ==================== Statistics ====================

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.country.id = :countryId")
    Long countByCountry(@Param("countryId") Long countryId);

    @Query("SELECT s.country.id, COUNT(s) FROM Satellite s GROUP BY s.country.id ORDER BY COUNT(s) DESC")
    List<Object[]> countSatellitesByCountry();

    @Query("SELECT s.satelliteType, COUNT(s) FROM Satellite s GROUP BY s.satelliteType ORDER BY COUNT(s) DESC")
    List<Object[]> countSatellitesByType();

    @Query("SELECT s.orbitType, COUNT(s) FROM Satellite s WHERE s.orbitType IS NOT NULL GROUP BY s.orbitType ORDER BY COUNT(s) DESC")
    List<Object[]> countSatellitesByOrbit();

    @Query("SELECT s.constellation, COUNT(s) FROM Satellite s WHERE s.constellation IS NOT NULL GROUP BY s.constellation ORDER BY COUNT(s) DESC")
    List<Object[]> countSatellitesByConstellation();

    @Query("SELECT s.launchYear, COUNT(s) FROM Satellite s WHERE s.launchYear IS NOT NULL GROUP BY s.launchYear ORDER BY s.launchYear ASC")
    List<Object[]> countSatellitesByYear();

    // BE-011: Additional aggregate queries
    @Query("SELECT COUNT(DISTINCT s.constellation) FROM Satellite s WHERE s.constellation IS NOT NULL")
    Long countConstellations();

    @Query("SELECT COUNT(s) FROM Satellite s WHERE s.launchYear = :year")
    Long countByLaunchYear(@Param("year") Integer year);

    // ==================== Search ====================

    @Query("SELECT s FROM Satellite s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.alternateName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.noradId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.cosparId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.operator) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.constellation) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY s.launchDate DESC")
    List<Satellite> searchSatellites(@Param("query") String query);

    // ==================== Special Queries ====================

    @Query("SELECT s FROM Satellite s WHERE s.satelliteType = 'NAVIGATION' OR s.satelliteType = 'GPS' OR " +
           "s.satelliteType = 'GLONASS' OR s.satelliteType = 'GALILEO' OR s.satelliteType = 'BEIDOU' " +
           "ORDER BY s.constellation, s.launchDate DESC")
    List<Satellite> findNavigationSatellites();

    @Query("SELECT s FROM Satellite s WHERE s.satelliteType = 'SPACE_STATION' OR s.satelliteType = 'SPACE_STATION_MODULE' " +
           "ORDER BY s.launchDate DESC")
    List<Satellite> findSpaceStations();

    @Query("SELECT s FROM Satellite s WHERE s.orbitType = 'GEO' OR s.orbitType = 'GSO' ORDER BY s.geoLongitude ASC")
    List<Satellite> findGeostationarySatellites();
}
