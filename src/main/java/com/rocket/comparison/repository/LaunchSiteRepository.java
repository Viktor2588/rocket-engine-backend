package com.rocket.comparison.repository;

import com.rocket.comparison.entity.LaunchSite;
import com.rocket.comparison.entity.LaunchSiteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaunchSiteRepository extends JpaRepository<LaunchSite, Long> {

    // ==================== By Name ====================

    Optional<LaunchSite> findByShortName(String shortName);

    Optional<LaunchSite> findByNameIgnoreCase(String name);

    // ==================== By Country ====================

    List<LaunchSite> findByCountryIdOrderByNameAsc(Long countryId);

    List<LaunchSite> findByCountryIsoCodeOrderByNameAsc(String isoCode);

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.country.id = :countryId AND ls.status = :status ORDER BY ls.name ASC")
    List<LaunchSite> findByCountryAndStatus(@Param("countryId") Long countryId, @Param("status") LaunchSiteStatus status);

    // ==================== By Status ====================

    List<LaunchSite> findByStatusOrderByNameAsc(LaunchSiteStatus status);

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.status = 'OPERATIONAL' OR ls.status = 'PARTIAL' ORDER BY ls.totalLaunches DESC")
    List<LaunchSite> findActiveLaunchSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.status = 'OPERATIONAL' OR ls.status = 'PARTIAL'")
    Long countActiveLaunchSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.country.id = :countryId AND (ls.status = 'OPERATIONAL' OR ls.status = 'PARTIAL')")
    Long countActiveLaunchSitesByCountry(@Param("countryId") Long countryId);

    // ==================== By Capabilities ====================

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.humanRatedCapable = true ORDER BY ls.totalLaunches DESC")
    List<LaunchSite> findHumanRatedSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.humanRatedCapable = true")
    Long countHumanRatedSites();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.supportsInterplanetary = true ORDER BY ls.name ASC")
    List<LaunchSite> findInterplanetaryCapableSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.supportsInterplanetary = true")
    Long countInterplanetaryCapableSites();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.supportsGeo = true ORDER BY ls.name ASC")
    List<LaunchSite> findGeoCapableSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.supportsGeo = true")
    Long countGeoCapableSites();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.supportsPolar = true OR ls.supportsSso = true ORDER BY ls.name ASC")
    List<LaunchSite> findPolarCapableSites();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.supportsPolar = true OR ls.supportsSso = true")
    Long countPolarCapableSites();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.hasLandingFacilities = true ORDER BY ls.name ASC")
    List<LaunchSite> findSitesWithLandingFacilities();

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.hasLandingFacilities = true")
    Long countSitesWithLandingFacilities();

    // ==================== By Location ====================

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.latitude BETWEEN :minLat AND :maxLat ORDER BY ls.latitude ASC")
    List<LaunchSite> findByLatitudeRange(@Param("minLat") Double minLatitude, @Param("maxLat") Double maxLatitude);

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.region = :region ORDER BY ls.name ASC")
    List<LaunchSite> findByRegion(@Param("region") String region);

    @Query("SELECT DISTINCT ls.region FROM LaunchSite ls WHERE ls.region IS NOT NULL ORDER BY ls.region ASC")
    List<String> findAllRegions();

    // ==================== Statistics ====================

    @Query("SELECT COUNT(ls) FROM LaunchSite ls WHERE ls.country.id = :countryId")
    Long countByCountry(@Param("countryId") Long countryId);

    @Query("SELECT ls.country.id, COUNT(ls) FROM LaunchSite ls GROUP BY ls.country.id ORDER BY COUNT(ls) DESC")
    List<Object[]> countSitesByCountry();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.totalLaunches IS NOT NULL ORDER BY ls.totalLaunches DESC")
    List<LaunchSite> findByMostLaunches();

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.successRate IS NOT NULL ORDER BY ls.successRate DESC")
    List<LaunchSite> findByHighestSuccessRate();

    @Query("SELECT SUM(ls.totalLaunches) FROM LaunchSite ls WHERE ls.country.id = :countryId")
    Long sumLaunchesByCountry(@Param("countryId") Long countryId);

    // ==================== Timeline ====================

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.establishedYear = :year ORDER BY ls.name ASC")
    List<LaunchSite> findByEstablishedYear(@Param("year") Integer year);

    @Query("SELECT ls FROM LaunchSite ls WHERE ls.establishedYear BETWEEN :startYear AND :endYear ORDER BY ls.establishedYear ASC")
    List<LaunchSite> findByEstablishedYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    @Query("SELECT DISTINCT ls.establishedYear FROM LaunchSite ls WHERE ls.establishedYear IS NOT NULL ORDER BY ls.establishedYear ASC")
    List<Integer> findAllEstablishedYears();

    // ==================== Search ====================

    @Query("SELECT ls FROM LaunchSite ls WHERE " +
           "LOWER(ls.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(ls.shortName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(ls.alternateName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(ls.operator) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(ls.region) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY ls.name ASC")
    List<LaunchSite> searchLaunchSites(@Param("query") String query);

    // ==================== Operator ====================

    @Query("SELECT DISTINCT ls.operator FROM LaunchSite ls WHERE ls.operator IS NOT NULL ORDER BY ls.operator ASC")
    List<String> findAllOperators();

    List<LaunchSite> findByOperatorOrderByNameAsc(String operator);
}
