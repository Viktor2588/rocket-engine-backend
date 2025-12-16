package com.rocket.comparison.repository;

import com.rocket.comparison.entity.Country;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // Step 2.2: Entity graph methods to avoid N+1 queries when fetching with engines
    @EntityGraph(attributePaths = {"engines"})
    @Query("SELECT c FROM Country c")
    List<Country> findAllWithEngines();

    @EntityGraph(attributePaths = {"engines"})
    @Query("SELECT c FROM Country c WHERE c.id = :id")
    Optional<Country> findByIdWithEngines(@Param("id") Long id);

    @EntityGraph(attributePaths = {"engines"})
    @Query("SELECT c FROM Country c WHERE c.isoCode = :isoCode")
    Optional<Country> findByIsoCodeWithEngines(@Param("isoCode") String isoCode);

    Optional<Country> findByIsoCode(String isoCode);

    Optional<Country> findByName(String name);

    List<Country> findByRegion(String region);

    // Capability-based queries
    List<Country> findByHumanSpaceflightCapableTrue();

    List<Country> findByIndependentLaunchCapableTrue();

    List<Country> findByReusableRocketCapableTrue();

    List<Country> findByDeepSpaceCapableTrue();

    List<Country> findBySpaceStationCapableTrue();

    // Rankings
    @Query("SELECT c FROM Country c WHERE c.overallCapabilityScore IS NOT NULL ORDER BY c.overallCapabilityScore DESC")
    List<Country> findAllOrderByCapabilityScoreDesc();

    @Query("SELECT c FROM Country c WHERE c.overallCapabilityScore >= :minScore ORDER BY c.overallCapabilityScore DESC")
    List<Country> findByMinCapabilityScore(@Param("minScore") Double minScore);

    // Statistics queries
    @Query("SELECT COUNT(c) FROM Country c WHERE c.independentLaunchCapable = true")
    Long countWithLaunchCapability();

    @Query("SELECT COUNT(c) FROM Country c WHERE c.humanSpaceflightCapable = true")
    Long countWithHumanSpaceflight();

    @Query("SELECT c FROM Country c WHERE c.totalLaunches IS NOT NULL ORDER BY c.totalLaunches DESC")
    List<Country> findTopByTotalLaunches();

    @Query("SELECT c FROM Country c WHERE c.launchSuccessRate IS NOT NULL ORDER BY c.launchSuccessRate DESC")
    List<Country> findTopBySuccessRate();

    // Optimized budget analytics query
    @Query("SELECT c FROM Country c WHERE c.annualBudgetUsd IS NOT NULL AND c.annualBudgetUsd > 0 ORDER BY c.annualBudgetUsd DESC")
    List<Country> findCountriesWithBudgetOrderByBudgetDesc();

    // Optimized query for emerging nations - exclude high scorers
    @Query("SELECT c FROM Country c WHERE c.overallCapabilityScore IS NULL OR c.overallCapabilityScore <= :threshold")
    List<Country> findPotentialEmergingNations(@Param("threshold") Double threshold);

    // BE-052: Additional COUNT queries for analytics
    @Query("SELECT COUNT(c) FROM Country c WHERE c.reusableRocketCapable = true")
    Long countWithReusableCapability();

    // BE-052: Records queries with LIMIT 1
    @Query("SELECT c FROM Country c WHERE c.totalLaunches IS NOT NULL ORDER BY c.totalLaunches DESC LIMIT 1")
    Optional<Country> findCountryWithMostLaunches();

    @Query("SELECT c FROM Country c WHERE c.totalLaunches IS NOT NULL AND c.totalLaunches >= :minLaunches AND c.launchSuccessRate IS NOT NULL ORDER BY c.launchSuccessRate DESC LIMIT 1")
    Optional<Country> findCountryWithHighestSuccessRate(@Param("minLaunches") Integer minLaunches);

    @Query("SELECT c FROM Country c WHERE c.overallCapabilityScore IS NOT NULL ORDER BY c.overallCapabilityScore DESC LIMIT 1")
    Optional<Country> findCountryWithHighestCapabilityScore();
}
