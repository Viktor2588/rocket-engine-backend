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
}
