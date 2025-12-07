package com.rocket.comparison.repository;

import com.rocket.comparison.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

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
}
