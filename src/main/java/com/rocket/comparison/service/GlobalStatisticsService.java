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

        // Capability metrics - BE-011: Use COUNT queries instead of findAll().size()
        overview.put("humanRatedLaunchSites", launchSiteRepository.countHumanRatedSites());
        overview.put("interplanetaryCapableSites", launchSiteRepository.countInterplanetaryCapableSites());

        // Time-based metrics - BE-011: Use COUNT query
        int currentYear = LocalDate.now().getYear();
        overview.put("missionsThisYear", spaceMissionRepository.countByLaunchYear(currentYear));

        overview.put("generatedAt", LocalDate.now().toString());

        return overview;
    }

    /**
     * Get entity counts broken down by type
     */
    public Map<String, Object> getEntityCounts() {
        Map<String, Object> counts = new LinkedHashMap<>();

        // BE-011: Use COUNT queries instead of findAll().size()
        counts.put("countries", Map.of(
            "total", countryRepository.count(),
            "withLaunchCapability", countryRepository.countWithLaunchCapability(),
            "withHumanSpaceflight", countryRepository.countWithHumanSpaceflight()
        ));

        counts.put("engines", Map.of(
            "total", engineRepository.count()
        ));

        counts.put("satellites", Map.of(
            "total", satelliteRepository.count(),
            "active", satelliteRepository.countActiveSatellites()
        ));

        // BE-011: Use COUNT query
        counts.put("launchSites", Map.of(
            "total", launchSiteRepository.count(),
            "active", launchSiteRepository.countActiveLaunchSites(),
            "humanRated", launchSiteRepository.countHumanRatedSites()
        ));

        // BE-011: Use COUNT query
        counts.put("missions", Map.of(
            "total", spaceMissionRepository.count(),
            "crewed", spaceMissionRepository.countCrewedMissions()
        ));

        // BE-011: Use COUNT query
        counts.put("milestones", Map.of(
            "total", spaceMilestoneRepository.count(),
            "firsts", spaceMilestoneRepository.countGlobalFirsts()
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
     * BE-011: Use DB GROUP BY and aggregate queries instead of findAll()
     */
    public Map<String, Object> getEngineTechnologyStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // By propellant type - use GROUP BY query
        Map<String, Long> byPropellant = new LinkedHashMap<>();
        engineRepository.countByPropellant().forEach(row -> {
            String propellant = row[0] != null ? (String) row[0] : "Unknown";
            Long count = (Long) row[1];
            byPropellant.put(propellant, count);
        });
        stats.put("byPropellantType", byPropellant);

        // By cycle type - use GROUP BY query
        Map<String, Long> byCycle = new LinkedHashMap<>();
        engineRepository.countByPowerCycle().forEach(row -> {
            String cycle = row[0] != null ? (String) row[0] : "Unknown";
            Long count = (Long) row[1];
            byCycle.put(cycle, count);
        });
        stats.put("byCycleType", byCycle);

        // By status - use GROUP BY query
        Map<String, Long> byStatus = new LinkedHashMap<>();
        engineRepository.countByStatus().forEach(row -> {
            String status = row[0] != null ? (String) row[0] : "Unknown";
            Long count = (Long) row[1];
            byStatus.put(status, count);
        });
        stats.put("byStatus", byStatus);

        // Performance metrics - use DB aggregate queries
        Double maxThrust = engineRepository.findMaxThrust();
        Double maxIsp = engineRepository.findMaxIsp();
        Double avgThrust = engineRepository.findAvgThrust();

        stats.put("highestThrustN", maxThrust != null ? maxThrust : 0.0);
        stats.put("highestIspS", maxIsp != null ? maxIsp : 0.0);
        stats.put("averageThrustN", avgThrust != null ? avgThrust : 0.0);

        return stats;
    }

    /**
     * Get satellite technology breakdown
     * BE-011: Use COUNT query instead of findAll().size()
     */
    public Map<String, Object> getSatelliteTechnologyStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // By type
        stats.put("byType", satelliteRepository.countSatellitesByType());

        // By orbit
        stats.put("byOrbit", satelliteRepository.countSatellitesByOrbit());

        // Constellation statistics - use COUNT query
        stats.put("constellationCount", satelliteRepository.countConstellations());

        return stats;
    }

    // ==================== Infrastructure Statistics ====================

    /**
     * Get launch infrastructure statistics
     * BE-011: Use COUNT queries instead of findAll().size()
     */
    public Map<String, Object> getLaunchInfrastructureStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Global counts
        stats.put("totalLaunchSites", launchSiteRepository.count());
        stats.put("activeSites", launchSiteRepository.countActiveLaunchSites());

        // Capability breakdown - use COUNT queries
        stats.put("humanRatedSites", launchSiteRepository.countHumanRatedSites());
        stats.put("interplanetaryCapable", launchSiteRepository.countInterplanetaryCapableSites());
        stats.put("geoCapable", launchSiteRepository.countGeoCapableSites());
        stats.put("polarCapable", launchSiteRepository.countPolarCapableSites());
        stats.put("sitesWithLanding", launchSiteRepository.countSitesWithLandingFacilities());

        // By country
        stats.put("sitesByCountry", launchSiteRepository.countSitesByCountry());

        // All regions
        stats.put("regions", launchSiteRepository.findAllRegions());

        return stats;
    }

    // ==================== Mission Statistics ====================

    /**
     * Get mission statistics
     * BE-011: Use COUNT query instead of findAll().size()
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

        // Crewed vs uncrewed - use COUNT query
        long crewed = spaceMissionRepository.countCrewedMissions();
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
     * BE-011: Use GROUP BY queries instead of findAll()
     */
    public Map<String, Object> getStatsByDecade() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Milestones grouped by decade - use GROUP BY query
        Map<String, Long> milestonesByDecade = new LinkedHashMap<>();
        spaceMilestoneRepository.countByDecade().forEach(row -> {
            Integer decade = (Integer) row[0];
            Long count = (Long) row[1];
            if (decade != null) {
                milestonesByDecade.put(decade + "s", count);
            }
        });
        stats.put("milestonesByDecade", milestonesByDecade);

        // Missions grouped by decade - use GROUP BY query
        Map<String, Long> missionsByDecade = new LinkedHashMap<>();
        spaceMissionRepository.countMissionsByDecade().forEach(row -> {
            Integer decade = (Integer) row[0];
            Long count = (Long) row[1];
            if (decade != null) {
                missionsByDecade.put(decade + "s", count);
            }
        });
        stats.put("missionsByDecade", missionsByDecade);

        // Also include launches by decade from country data
        // We only have total launch counts, not by decade, so derive from missions
        stats.put("launchesByDecade", missionsByDecade);

        return stats;
    }

    /**
     * Get year-over-year growth statistics
     * BE-011: Use COUNT queries instead of findByLaunchYear().size()
     */
    public Map<String, Object> getYearOverYearGrowth() {
        Map<String, Object> growth = new LinkedHashMap<>();

        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear - 1;

        // Missions - use COUNT query
        long missionsThisYear = spaceMissionRepository.countByLaunchYear(currentYear);
        long missionsLastYear = spaceMissionRepository.countByLaunchYear(previousYear);
        double missionGrowth = missionsLastYear > 0 ?
            ((double)(missionsThisYear - missionsLastYear) / missionsLastYear * 100) : 0;

        growth.put("missions", Map.of(
            "currentYear", missionsThisYear,
            "previousYear", missionsLastYear,
            "growthPercent", Math.round(missionGrowth * 100.0) / 100.0
        ));

        // Satellites launched - use COUNT query
        long satellitesThisYear = satelliteRepository.countByLaunchYear(currentYear);
        long satellitesLastYear = satelliteRepository.countByLaunchYear(previousYear);
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
