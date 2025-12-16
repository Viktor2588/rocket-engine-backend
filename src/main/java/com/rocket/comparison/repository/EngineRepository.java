package com.rocket.comparison.repository;

import com.rocket.comparison.entity.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngineRepository extends JpaRepository<Engine, Long> {
    List<Engine> findByDesigner(String designer);
    List<Engine> findByPropellant(String propellant);
    List<Engine> findByThrustNGreaterThan(Long thrust);

    @Query("SELECT e FROM Engine e WHERE e.isp_s > :isp")
    List<Engine> findByIsp_sGreaterThan(@Param("isp") Double isp);

    // Country-based queries
    List<Engine> findByCountryId(Long countryId);

    List<Engine> findByCountryIsoCode(String isoCode);

    @Query("SELECT e FROM Engine e WHERE e.origin = :origin")
    List<Engine> findByOrigin(@Param("origin") String origin);

    @Query("SELECT COUNT(e) FROM Engine e WHERE e.country.id = :countryId")
    Long countByCountryId(@Param("countryId") Long countryId);

    // Optimized analytics queries - aggregate at database level
    @Query("SELECT e.propellant, COUNT(e) FROM Engine e GROUP BY e.propellant")
    List<Object[]> countByPropellant();

    @Query("SELECT e.powerCycle, COUNT(e) FROM Engine e GROUP BY e.powerCycle")
    List<Object[]> countByPowerCycle();

    // Records queries - get max values directly
    @Query("SELECT e FROM Engine e WHERE e.thrustN = (SELECT MAX(e2.thrustN) FROM Engine e2 WHERE e2.thrustN IS NOT NULL)")
    List<Engine> findEngineWithHighestThrust();

    @Query("SELECT e FROM Engine e WHERE e.isp_s = (SELECT MAX(e2.isp_s) FROM Engine e2 WHERE e2.isp_s IS NOT NULL)")
    List<Engine> findEngineWithHighestIsp();

    // BE-011: Aggregate statistics at database level
    @Query("SELECT e.status, COUNT(e) FROM Engine e GROUP BY e.status")
    List<Object[]> countByStatus();

    @Query("SELECT MAX(e.thrustN) FROM Engine e WHERE e.thrustN IS NOT NULL")
    Double findMaxThrust();

    @Query("SELECT MAX(e.isp_s) FROM Engine e WHERE e.isp_s IS NOT NULL")
    Double findMaxIsp();

    @Query("SELECT AVG(e.thrustN) FROM Engine e WHERE e.thrustN IS NOT NULL")
    Double findAvgThrust();
}
