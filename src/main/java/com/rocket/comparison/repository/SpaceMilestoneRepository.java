package com.rocket.comparison.repository;

import com.rocket.comparison.entity.MilestoneType;
import com.rocket.comparison.entity.SpaceMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceMilestoneRepository extends JpaRepository<SpaceMilestone, Long> {

    // ==================== By Country ====================

    List<SpaceMilestone> findByCountryIdOrderByDateAchievedAsc(Long countryId);

    List<SpaceMilestone> findByCountryIsoCodeOrderByDateAchievedAsc(String isoCode);

    @Query("SELECT m FROM SpaceMilestone m WHERE m.country.id = :countryId AND m.isGlobalFirst = true ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findFirstsByCountry(@Param("countryId") Long countryId);

    // ==================== By Milestone Type ====================

    List<SpaceMilestone> findByMilestoneTypeOrderByGlobalRankAsc(MilestoneType type);

    Optional<SpaceMilestone> findByMilestoneTypeAndGlobalRank(MilestoneType type, Integer rank);

    @Query("SELECT m FROM SpaceMilestone m WHERE m.milestoneType = :type AND m.globalRank = 1")
    Optional<SpaceMilestone> findFirstAchiever(@Param("type") MilestoneType type);

    // ==================== By Category ====================

    @Query("SELECT m FROM SpaceMilestone m WHERE m.milestoneType IN :types ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findByMilestoneTypes(@Param("types") List<MilestoneType> types);

    // ==================== Timeline Queries ====================

    List<SpaceMilestone> findByYearOrderByDateAchievedAsc(Integer year);

    List<SpaceMilestone> findByDecadeOrderByDateAchievedAsc(Integer decade);

    List<SpaceMilestone> findByEraOrderByDateAchievedAsc(String era);

    @Query("SELECT m FROM SpaceMilestone m WHERE m.dateAchieved BETWEEN :start AND :end ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT m FROM SpaceMilestone m WHERE m.year BETWEEN :startYear AND :endYear ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findByYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    // ==================== Global Firsts ====================

    @Query("SELECT m FROM SpaceMilestone m WHERE m.globalRank = 1 ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findAllGlobalFirsts();

    @Query("SELECT m FROM SpaceMilestone m WHERE m.isGlobalFirst = true ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> findFirstAchievers();

    // ==================== Statistics ====================

    @Query("SELECT COUNT(m) FROM SpaceMilestone m WHERE m.country.id = :countryId")
    Long countByCountry(@Param("countryId") Long countryId);

    @Query("SELECT COUNT(m) FROM SpaceMilestone m WHERE m.country.id = :countryId AND m.globalRank = 1")
    Long countFirstsByCountry(@Param("countryId") Long countryId);

    @Query("SELECT m.country.id, COUNT(m) FROM SpaceMilestone m WHERE m.globalRank = 1 GROUP BY m.country.id ORDER BY COUNT(m) DESC")
    List<Object[]> countFirstsByCountryRanked();

    @Query("SELECT DISTINCT m.year FROM SpaceMilestone m ORDER BY m.year ASC")
    List<Integer> findAllYears();

    @Query("SELECT DISTINCT m.decade FROM SpaceMilestone m ORDER BY m.decade ASC")
    List<Integer> findAllDecades();

    @Query("SELECT DISTINCT m.era FROM SpaceMilestone m ORDER BY m.era ASC")
    List<String> findAllEras();

    // ==================== Search ====================

    @Query("SELECT m FROM SpaceMilestone m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.achievedBy) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.missionName) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY m.dateAchieved ASC")
    List<SpaceMilestone> searchMilestones(@Param("query") String query);
}
