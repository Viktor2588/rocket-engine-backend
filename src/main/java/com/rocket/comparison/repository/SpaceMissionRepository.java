package com.rocket.comparison.repository;

import com.rocket.comparison.entity.Destination;
import com.rocket.comparison.entity.MissionStatus;
import com.rocket.comparison.entity.MissionType;
import com.rocket.comparison.entity.SpaceMission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceMissionRepository extends JpaRepository<SpaceMission, Long> {

    // Step 2.2: Entity graph methods to avoid N+1 queries when fetching with country
    @EntityGraph(attributePaths = {"country"})
    @Query("SELECT m FROM SpaceMission m ORDER BY m.launchDate DESC")
    List<SpaceMission> findAllWithCountry();

    @EntityGraph(attributePaths = {"country"})
    @Query("SELECT m FROM SpaceMission m WHERE m.id = :id")
    Optional<SpaceMission> findByIdWithCountry(@Param("id") Long id);

    // ==================== By Country ====================

    List<SpaceMission> findByCountryIdOrderByLaunchDateDesc(Long countryId);

    List<SpaceMission> findByCountryIsoCodeOrderByLaunchDateDesc(String isoCode);

    @Query("SELECT m FROM SpaceMission m WHERE m.country.id = :countryId AND m.status = :status ORDER BY m.launchDate DESC")
    List<SpaceMission> findByCountryAndStatus(@Param("countryId") Long countryId, @Param("status") MissionStatus status);

    // ==================== By Status ====================

    List<SpaceMission> findByStatusOrderByLaunchDateDesc(MissionStatus status);

    @Query("SELECT m FROM SpaceMission m WHERE m.status IN :statuses ORDER BY m.launchDate DESC")
    List<SpaceMission> findByStatuses(@Param("statuses") List<MissionStatus> statuses);

    @Query("SELECT m FROM SpaceMission m WHERE m.status = 'ACTIVE' OR m.status = 'LAUNCHED' ORDER BY m.launchDate DESC")
    List<SpaceMission> findActiveMissions();

    @Query("SELECT m FROM SpaceMission m WHERE m.status = 'PLANNED' OR m.status = 'IN_DEVELOPMENT' ORDER BY m.launchDate ASC")
    List<SpaceMission> findUpcomingMissions();

    // ==================== By Mission Type ====================

    List<SpaceMission> findByMissionTypeOrderByLaunchDateDesc(MissionType missionType);

    @Query("SELECT m FROM SpaceMission m WHERE m.missionType IN :types ORDER BY m.launchDate DESC")
    List<SpaceMission> findByMissionTypes(@Param("types") List<MissionType> types);

    @Query("SELECT m FROM SpaceMission m WHERE m.missionCategory = :category ORDER BY m.launchDate DESC")
    List<SpaceMission> findByMissionCategory(@Param("category") String category);

    // ==================== By Destination ====================

    List<SpaceMission> findByDestinationOrderByLaunchDateDesc(Destination destination);

    @Query("SELECT m FROM SpaceMission m WHERE m.destination IN :destinations ORDER BY m.launchDate DESC")
    List<SpaceMission> findByDestinations(@Param("destinations") List<Destination> destinations);

    // ==================== Crewed Missions ====================

    @Query("SELECT m FROM SpaceMission m WHERE m.crewed = true ORDER BY m.launchDate DESC")
    List<SpaceMission> findCrewedMissions();

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.crewed = true")
    Long countCrewedMissions();

    @Query("SELECT m FROM SpaceMission m WHERE m.crewed = true AND m.country.id = :countryId ORDER BY m.launchDate DESC")
    List<SpaceMission> findCrewedMissionsByCountry(@Param("countryId") Long countryId);

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.crewed = true AND m.country.id = :countryId")
    Long countCrewedMissionsByCountry(@Param("countryId") Long countryId);

    // ==================== Historic Firsts ====================

    @Query("SELECT m FROM SpaceMission m WHERE m.isHistoricFirst = true ORDER BY m.launchDate ASC")
    List<SpaceMission> findHistoricFirsts();

    @Query("SELECT m FROM SpaceMission m WHERE m.isHistoricFirst = true AND m.country.id = :countryId ORDER BY m.launchDate ASC")
    List<SpaceMission> findHistoricFirstsByCountry(@Param("countryId") Long countryId);

    // ==================== Timeline Queries ====================

    @Query("SELECT m FROM SpaceMission m WHERE m.launchYear = :year ORDER BY m.launchDate ASC")
    List<SpaceMission> findByLaunchYear(@Param("year") Integer year);

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.launchYear = :year")
    Long countByLaunchYear(@Param("year") Integer year);

    @Query("SELECT m FROM SpaceMission m WHERE m.launchDecade = :decade ORDER BY m.launchDate ASC")
    List<SpaceMission> findByLaunchDecade(@Param("decade") Integer decade);

    @Query("SELECT m.launchDecade, COUNT(m) FROM SpaceMission m WHERE m.launchDecade IS NOT NULL GROUP BY m.launchDecade ORDER BY m.launchDecade ASC")
    List<Object[]> countMissionsByDecade();

    @Query("SELECT m FROM SpaceMission m WHERE m.launchDate BETWEEN :startDate AND :endDate ORDER BY m.launchDate ASC")
    List<SpaceMission> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM SpaceMission m WHERE m.launchYear BETWEEN :startYear AND :endYear ORDER BY m.launchDate ASC")
    List<SpaceMission> findByYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    // ==================== Statistics ====================

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.country.id = :countryId")
    Long countByCountry(@Param("countryId") Long countryId);

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.country.id = :countryId AND m.status = 'COMPLETED'")
    Long countSuccessfulByCountry(@Param("countryId") Long countryId);

    @Query("SELECT COUNT(m) FROM SpaceMission m WHERE m.country.id = :countryId AND (m.status = 'FAILED' OR m.status = 'LOST')")
    Long countFailedByCountry(@Param("countryId") Long countryId);

    @Query("SELECT m.country.id, COUNT(m) FROM SpaceMission m GROUP BY m.country.id ORDER BY COUNT(m) DESC")
    List<Object[]> countMissionsByCountry();

    @Query("SELECT m.missionType, COUNT(m) FROM SpaceMission m GROUP BY m.missionType ORDER BY COUNT(m) DESC")
    List<Object[]> countMissionsByType();

    @Query("SELECT m.destination, COUNT(m) FROM SpaceMission m WHERE m.destination IS NOT NULL GROUP BY m.destination ORDER BY COUNT(m) DESC")
    List<Object[]> countMissionsByDestination();

    @Query("SELECT m.launchYear, COUNT(m) FROM SpaceMission m WHERE m.launchYear IS NOT NULL GROUP BY m.launchYear ORDER BY m.launchYear ASC")
    List<Object[]> countMissionsByYear();

    // BE-053: Group by country and year for analytics
    @Query("SELECT m.country.id, m.country.name, m.country.isoCode, m.launchYear, COUNT(m) " +
           "FROM SpaceMission m WHERE m.country IS NOT NULL AND m.launchYear IS NOT NULL " +
           "GROUP BY m.country.id, m.country.name, m.country.isoCode, m.launchYear " +
           "ORDER BY COUNT(m) DESC, m.launchYear ASC")
    List<Object[]> countMissionsByCountryAndYear();

    // ==================== Lists ====================

    @Query("SELECT DISTINCT m.launchYear FROM SpaceMission m WHERE m.launchYear IS NOT NULL ORDER BY m.launchYear ASC")
    List<Integer> findAllLaunchYears();

    @Query("SELECT DISTINCT m.launchDecade FROM SpaceMission m WHERE m.launchDecade IS NOT NULL ORDER BY m.launchDecade ASC")
    List<Integer> findAllLaunchDecades();

    @Query("SELECT DISTINCT m.missionCategory FROM SpaceMission m WHERE m.missionCategory IS NOT NULL ORDER BY m.missionCategory ASC")
    List<String> findAllMissionCategories();

    @Query("SELECT DISTINCT m.operator FROM SpaceMission m WHERE m.operator IS NOT NULL ORDER BY m.operator ASC")
    List<String> findAllOperators();

    @Query("SELECT DISTINCT m.launchSite FROM SpaceMission m WHERE m.launchSite IS NOT NULL ORDER BY m.launchSite ASC")
    List<String> findAllLaunchSites();

    // ==================== Search ====================

    @Query("SELECT m FROM SpaceMission m WHERE " +
           "LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.missionDesignation) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.alternateName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.crewNames) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.commander) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.operator) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY m.launchDate DESC")
    List<SpaceMission> searchMissions(@Param("query") String query);

    // ==================== Advanced Queries ====================

    @Query("SELECT m FROM SpaceMission m WHERE m.destination = :destination AND m.status = 'COMPLETED' ORDER BY m.launchDate ASC")
    List<SpaceMission> findSuccessfulMissionsByDestination(@Param("destination") Destination destination);

    @Query("SELECT m FROM SpaceMission m WHERE m.missionType = :type AND m.country.id = :countryId ORDER BY m.launchDate DESC")
    List<SpaceMission> findByTypeAndCountry(@Param("type") MissionType type, @Param("countryId") Long countryId);

    @Query("SELECT m FROM SpaceMission m WHERE m.sampleReturnMassKg > 0 ORDER BY m.sampleReturnMassKg DESC")
    List<SpaceMission> findSampleReturnMissions();

    @Query("SELECT m FROM SpaceMission m WHERE m.evaCount > 0 ORDER BY m.evaCount DESC")
    List<SpaceMission> findMissionsWithEVA();

    // ==================== Records ====================

    @Query("SELECT m FROM SpaceMission m WHERE m.durationDays IS NOT NULL ORDER BY m.durationDays DESC")
    List<SpaceMission> findLongestMissions();

    @Query("SELECT m FROM SpaceMission m WHERE m.maxDistanceFromEarthKm IS NOT NULL ORDER BY m.maxDistanceFromEarthKm DESC")
    List<SpaceMission> findFarthestMissions();

    @Query("SELECT m FROM SpaceMission m WHERE m.crewSize IS NOT NULL ORDER BY m.crewSize DESC")
    List<SpaceMission> findLargestCrewMissions();
}
