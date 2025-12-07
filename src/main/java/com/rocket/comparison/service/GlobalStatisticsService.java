package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service providing comprehensive global statistics across all space-related entities.
 * Aggregates data from countries, engines, satellites, launch sites, missions, and milestones.
 */
@Service
@RequiredArgsConstructor
public class GlobalStatisticsService {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;

    // ==================== Overview Statistics ====================

    /**
     * Get comprehensive overview of all space-related statistics
     */
    public Map<String, Object> getGlobalOverview() {
        Map<String, Object> overview = new LinkedHashMap<>();

        // Entity counts
        overview.put("totalCountries", countryRepository.count());
        overview.put("totalEngines", engineRepository.count());
        overview.put("totalSatellites", satelliteRepository.count());
        overview.put("totalLaunchSites", launchSiteRepository.count());
        overview.put("totalMissions", spaceMissionRepository.count());
        overview.put("totalMilestones", spaceMilestoneRepository.count());

        // Active counts
        overview.put("activeSatellites", satelliteRepository.countActiveSatellites());
        overview.put("activeLaunchSites", launchSiteRepository.countActiveLaunchSites());

        // Capability metrics
        overview.put("humanRatedLaunchSites", launchSiteRepository.findHumanRatedSites().size());
        overview.put("interplanetaryCapableSites", launchSiteRepository.findInterplanetaryCapableSites().size());

        // Time-based metrics
        int currentYear = LocalDate.now().getYear();
        overview.put("missionsThisYear", spaceMissionRepository.findByLaunchYear(currentYear).size());

        overview.put("generatedAt", LocalDate.now().toString());

        return overview;
    }

    /**
     * Get entity counts broken down by type
     */
    public Map<String, Object> getEntityCounts() {
        Map<String, Object> counts = new LinkedHashMap<>();

        counts.put("countries", Map.of(
            "total", countryRepository.count(),
            "withLaunchCapability", countryRepository.findByIndependentLaunchCapableTrue().size(),
            "withHumanSpaceflight", countryRepository.findByHumanSpaceflightCapableTrue().size()
        ));

        counts.put("engines", Map.of(
            "total", engineRepository.count()
        ));

        counts.put("satellites", Map.of(
            "total", satelliteRepository.count(),
            "active", satelliteRepository.countActiveSatellites()
        ));

        counts.put("launchSites", Map.of(
            "total", launchSiteRepository.count(),
            "active", launchSiteRepository.countActiveLaunchSites(),
            "humanRated", launchSiteRepository.findHumanRatedSites().size()
        ));

        counts.put("missions", Map.of(
            "total", spaceMissionRepository.count(),
            "crewed", spaceMissionRepository.findCrewedMissions().size()
        ));

        counts.put("milestones", Map.of(
            "total", spaceMilestoneRepository.count(),
            "firsts", spaceMilestoneRepository.findAllGlobalFirsts().size()
        ));

        return counts;
    }

    // ==================== Country Statistics ====================

    /**
     * Get statistics aggregated by country
     */
    public List<Map<String, Object>> getCountryStatistics() {
        List<Map<String, Object>> stats = new ArrayList<>();

        countryRepository.findAll().forEach(country -> {
            Map<String, Object> countryStat = new LinkedHashMap<>();
            countryStat.put("id", country.getId());
            countryStat.put("name", country.getName());
            countryStat.put("isoCode", country.getIsoCode());
            countryStat.put("flagUrl", country.getFlagUrl());

            // Counts
            countryStat.put("engineCount", engineRepository.countByCountryId(country.getId()));
            countryStat.put("satelliteCount", satelliteRepository.countByCountry(country.getId()));
            countryStat.put("launchSiteCount", launchSiteRepository.countByCountry(country.getId()));
            countryStat.put("missionCount", spaceMissionRepository.countByCountry(country.getId()));
            countryStat.put("milestoneCount", spaceMilestoneRepository.countByCountry(country.getId()));

            // Capability score
            if (country.getOverallCapabilityScore() != null) {
                countryStat.put("capabilityScore", country.getOverallCapabilityScore());
            }

            stats.add(countryStat);
        });

        // Sort by capability score descending
        stats.sort((a, b) -> {
            Double scoreA = (Double) a.getOrDefault("capabilityScore", 0.0);
            Double scoreB = (Double) b.getOrDefault("capabilityScore", 0.0);
            return scoreB.compareTo(scoreA);
        });

        return stats;
    }

    /**
     * Get top countries by various metrics
     */
    public Map<String, Object> getTopCountries(int limit) {
        Map<String, Object> rankings = new LinkedHashMap<>();

        // By capability score
        List<Map<String, Object>> byCapability = getCountryStatistics().stream()
            .filter(c -> c.get("capabilityScore") != null)
            .limit(limit)
            .toList();
        rankings.put("byCapabilityScore", byCapability);

        // By mission count
        List<Map<String, Object>> byMissions = getCountryStatistics().stream()
            .sorted((a, b) -> {
                Long countA = (Long) a.getOrDefault("missionCount", 0L);
                Long countB = (Long) b.getOrDefault("missionCount", 0L);
                return countB.compareTo(countA);
            })
            .limit(limit)
            .toList();
        rankings.put("byMissionCount", byMissions);

        // By satellite count
        List<Map<String, Object>> bySatellites = getCountryStatistics().stream()
            .sorted((a, b) -> {
                Long countA = (Long) a.getOrDefault("satelliteCount", 0L);
                Long countB = (Long) b.getOrDefault("satelliteCount", 0L);
                return countB.compareTo(countA);
            })
            .limit(limit)
            .toList();
        rankings.put("bySatelliteCount", bySatellites);

        return rankings;
    }

    // ==================== Technology Statistics ====================

    /**
     * Get engine technology breakdown
     */
    public Map<String, Object> getEngineTechnologyStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // By propellant type
        Map<String, Long> byPropellant = new LinkedHashMap<>();
        engineRepository.findAll().forEach(engine -> {
            String propellant = engine.getPropellant() != null ? engine.getPropellant() : "Unknown";
            byPropellant.merge(propellant, 1L, Long::sum);
        });
        stats.put("byPropellantType", byPropellant);

        // By cycle type
        Map<String, Long> byCycle = new LinkedHashMap<>();
        engineRepository.findAll().forEach(engine -> {
            String cycle = engine.getPowerCycle() != null ? engine.getPowerCycle() : "Unknown";
            byCycle.merge(cycle, 1L, Long::sum);
        });
        stats.put("byCycleType", byCycle);

        // By status
        Map<String, Long> byStatus = new LinkedHashMap<>();
        engineRepository.findAll().forEach(engine -> {
            String status = engine.getStatus() != null ? engine.getStatus() : "Unknown";
            byStatus.merge(status, 1L, Long::sum);
        });
        stats.put("byStatus", byStatus);

        // Performance metrics calculated from data
        OptionalDouble maxThrust = engineRepository.findAll().stream()
            .filter(e -> e.getThrustN() != null)
            .mapToDouble(Engine::getThrustN)
            .max();
        OptionalDouble maxIsp = engineRepository.findAll().stream()
            .filter(e -> e.getIsp_s() != null)
            .mapToDouble(Engine::getIsp_s)
            .max();
        OptionalDouble avgThrust = engineRepository.findAll().stream()
            .filter(e -> e.getThrustN() != null)
            .mapToDouble(Engine::getThrustN)
            .average();

        stats.put("highestThrustN", maxThrust.orElse(0));
        stats.put("highestIspS", maxIsp.orElse(0));
        stats.put("averageThrustN", avgThrust.orElse(0));

        return stats;
    }

    /**
     * Get satellite technology breakdown
     */
    public Map<String, Object> getSatelliteTechnologyStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // By type
        stats.put("byType", satelliteRepository.countSatellitesByType());

        // By orbit
        stats.put("byOrbit", satelliteRepository.countSatellitesByOrbit());

        // Constellation statistics
        stats.put("constellationCount", satelliteRepository.findAllConstellations().size());

        return stats;
    }

    // ==================== Infrastructure Statistics ====================

    /**
     * Get launch infrastructure statistics
     */
    public Map<String, Object> getLaunchInfrastructureStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Global counts
        stats.put("totalLaunchSites", launchSiteRepository.count());
        stats.put("activeSites", launchSiteRepository.countActiveLaunchSites());

        // Capability breakdown
        stats.put("humanRatedSites", launchSiteRepository.findHumanRatedSites().size());
        stats.put("interplanetaryCapable", launchSiteRepository.findInterplanetaryCapableSites().size());
        stats.put("geoCapable", launchSiteRepository.findGeoCapableSites().size());
        stats.put("polarCapable", launchSiteRepository.findPolarCapableSites().size());
        stats.put("sitesWithLanding", launchSiteRepository.findSitesWithLandingFacilities().size());

        // By country
        stats.put("sitesByCountry", launchSiteRepository.countSitesByCountry());

        // All regions
        stats.put("regions", launchSiteRepository.findAllRegions());

        return stats;
    }

    // ==================== Mission Statistics ====================

    /**
     * Get mission statistics
     */
    public Map<String, Object> getMissionStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Overall metrics
        long total = spaceMissionRepository.count();
        stats.put("totalMissions", total);

        // By type
        stats.put("byMissionType", spaceMissionRepository.countMissionsByType());

        // By year
        stats.put("byYear", spaceMissionRepository.countMissionsByYear());

        // Crewed vs uncrewed
        long crewed = spaceMissionRepository.findCrewedMissions().size();
        stats.put("crewedMissions", crewed);
        stats.put("uncrewedMissions", total - crewed);

        return stats;
    }

    /**
     * Get mission success rates by country
     */
    public List<Map<String, Object>> getMissionSuccessRatesByCountry() {
        List<Map<String, Object>> rates = new ArrayList<>();

        countryRepository.findAll().forEach(country -> {
            Long totalMissions = spaceMissionRepository.countByCountry(country.getId());
            if (totalMissions > 0) {
                Long successful = spaceMissionRepository.countSuccessfulByCountry(country.getId());

                Map<String, Object> countryRate = new LinkedHashMap<>();
                countryRate.put("countryId", country.getId());
                countryRate.put("countryName", country.getName());
                countryRate.put("isoCode", country.getIsoCode());
                countryRate.put("totalMissions", totalMissions);
                countryRate.put("successfulMissions", successful);
                countryRate.put("successRate", Math.round((double) successful / totalMissions * 10000.0) / 100.0);

                rates.add(countryRate);
            }
        });

        // Sort by success rate descending
        rates.sort((a, b) -> {
            Double rateA = (Double) a.getOrDefault("successRate", 0.0);
            Double rateB = (Double) b.getOrDefault("successRate", 0.0);
            return rateB.compareTo(rateA);
        });

        return rates;
    }

    // ==================== Timeline Statistics ====================

    /**
     * Get historical statistics by decade with counts
     */
    public Map<String, Object> getStatsByDecade() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Milestones grouped by decade with counts
        Map<String, Long> milestonesByDecade = new LinkedHashMap<>();
        spaceMilestoneRepository.findAll().forEach(milestone -> {
            Integer decade = milestone.getDecade();
            if (decade != null) {
                String decadeLabel = decade + "s";
                milestonesByDecade.merge(decadeLabel, 1L, Long::sum);
            }
        });
        stats.put("milestonesByDecade", milestonesByDecade);

        // Missions grouped by decade with counts
        Map<String, Long> missionsByDecade = new LinkedHashMap<>();
        spaceMissionRepository.findAll().forEach(mission -> {
            Integer decade = mission.getLaunchDecade();
            if (decade != null) {
                String decadeLabel = decade + "s";
                missionsByDecade.merge(decadeLabel, 1L, Long::sum);
            }
        });
        stats.put("missionsByDecade", missionsByDecade);

        // Also include launches by decade from country data
        Map<String, Long> launchesByDecade = new LinkedHashMap<>();
        // We only have total launch counts, not by decade, so derive from missions
        stats.put("launchesByDecade", missionsByDecade);

        return stats;
    }

    /**
     * Get year-over-year growth statistics
     */
    public Map<String, Object> getYearOverYearGrowth() {
        Map<String, Object> growth = new LinkedHashMap<>();

        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear - 1;

        // Missions
        long missionsThisYear = spaceMissionRepository.findByLaunchYear(currentYear).size();
        long missionsLastYear = spaceMissionRepository.findByLaunchYear(previousYear).size();
        double missionGrowth = missionsLastYear > 0 ?
            ((double)(missionsThisYear - missionsLastYear) / missionsLastYear * 100) : 0;

        growth.put("missions", Map.of(
            "currentYear", missionsThisYear,
            "previousYear", missionsLastYear,
            "growthPercent", Math.round(missionGrowth * 100.0) / 100.0
        ));

        // Satellites launched
        long satellitesThisYear = satelliteRepository.findByLaunchYear(currentYear).size();
        long satellitesLastYear = satelliteRepository.findByLaunchYear(previousYear).size();
        double satelliteGrowth = satellitesLastYear > 0 ?
            ((double)(satellitesThisYear - satellitesLastYear) / satellitesLastYear * 100) : 0;

        growth.put("satellites", Map.of(
            "currentYear", satellitesThisYear,
            "previousYear", satellitesLastYear,
            "growthPercent", Math.round(satelliteGrowth * 100.0) / 100.0
        ));

        return growth;
    }
}
