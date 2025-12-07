package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service providing analytics and trend analysis across space-related data.
 * Supports historical analysis, trend detection, and record tracking.
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;
    private final CapabilityScoreRepository capabilityScoreRepository;

    // ==================== Launch Analytics ====================

    /**
     * Get global launches per year
     */
    public Map<String, Object> getLaunchesPerYear() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Convert List<Object[]> to Map<Integer, Long>
        List<Object[]> rawData = spaceMissionRepository.countMissionsByYear();
        Map<Integer, Long> missionsByYear = new LinkedHashMap<>();
        for (Object[] row : rawData) {
            Integer year = (Integer) row[0];
            Long count = (Long) row[1];
            missionsByYear.put(year, count);
        }
        result.put("missionsByYear", missionsByYear);

        // Calculate totals and averages
        long totalMissions = missionsByYear.values().stream().mapToLong(Long::longValue).sum();
        double avgPerYear = missionsByYear.isEmpty() ? 0 : (double) totalMissions / missionsByYear.size();

        result.put("totalMissions", totalMissions);
        result.put("averagePerYear", Math.round(avgPerYear * 100.0) / 100.0);
        result.put("yearsTracked", missionsByYear.size());

        // Find peak year
        missionsByYear.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> {
                result.put("peakYear", entry.getKey());
                result.put("peakYearCount", entry.getValue());
            });

        return result;
    }

    /**
     * Get launches per year broken down by country
     */
    public Map<String, Object> getLaunchesPerYearByCountry() {
        Map<String, Object> result = new LinkedHashMap<>();

        List<Map<String, Object>> countryData = new ArrayList<>();

        countryRepository.findAll().forEach(country -> {
            Long missionCount = spaceMissionRepository.countByCountry(country.getId());
            if (missionCount > 0) {
                Map<String, Object> countryStats = new LinkedHashMap<>();
                countryStats.put("countryId", country.getId());
                countryStats.put("countryName", country.getName());
                countryStats.put("isoCode", country.getIsoCode());
                countryStats.put("totalMissions", missionCount);

                // Get year breakdown for this country
                List<SpaceMission> missions = spaceMissionRepository.findByCountryIdOrderByLaunchDateDesc(country.getId());
                Map<Integer, Long> yearBreakdown = missions.stream()
                    .filter(m -> m.getLaunchDate() != null)
                    .collect(Collectors.groupingBy(
                        m -> m.getLaunchDate().getYear(),
                        Collectors.counting()
                    ));
                countryStats.put("byYear", yearBreakdown);

                countryData.add(countryStats);
            }
        });

        // Sort by total missions descending
        countryData.sort((a, b) -> Long.compare(
            (Long) b.get("totalMissions"),
            (Long) a.get("totalMissions")
        ));

        result.put("countries", countryData);
        result.put("totalCountries", countryData.size());

        return result;
    }

    // ==================== Capability Growth ====================

    /**
     * Track capability score growth for a country over time
     * Note: This requires historical data tracking; returns current snapshot
     */
    public Map<String, Object> getCapabilityGrowth(Long countryId) {
        Map<String, Object> result = new LinkedHashMap<>();

        Optional<Country> countryOpt = countryRepository.findById(countryId);
        if (countryOpt.isEmpty()) {
            return Map.of("error", "Country not found");
        }

        Country country = countryOpt.get();
        result.put("countryId", country.getId());
        result.put("countryName", country.getName());
        result.put("currentScore", country.getOverallCapabilityScore());

        // Get capability breakdown
        List<CapabilityScore> scores = capabilityScoreRepository.findByCountryId(countryId);
        List<Map<String, Object>> categoryScores = scores.stream()
            .map(s -> {
                Map<String, Object> cat = new LinkedHashMap<>();
                cat.put("category", s.getCategory().name());
                cat.put("displayName", s.getCategory().getDisplayName());
                cat.put("score", s.getScore());
                cat.put("ranking", s.getRanking());
                return cat;
            })
            .toList();
        result.put("categoryBreakdown", categoryScores);

        // Historical milestones as proxy for growth
        List<SpaceMilestone> milestones = spaceMilestoneRepository.findByCountryIdOrderByDateAchievedAsc(countryId);
        List<Map<String, Object>> milestoneTimeline = milestones.stream()
            .map(m -> {
                Map<String, Object> ms = new LinkedHashMap<>();
                ms.put("year", m.getDateAchieved() != null ? m.getDateAchieved().getYear() : null);
                ms.put("title", m.getTitle());
                ms.put("type", m.getMilestoneType().name());
                ms.put("isGlobalFirst", m.getIsGlobalFirst());
                return ms;
            })
            .toList();
        result.put("milestoneTimeline", milestoneTimeline);
        result.put("totalMilestones", milestones.size());
        result.put("globalFirsts", milestones.stream().filter(SpaceMilestone::getIsGlobalFirst).count());

        return result;
    }

    // ==================== Emerging Nations ====================

    /**
     * Identify emerging space powers based on recent activity
     */
    public Map<String, Object> getEmergingNations() {
        Map<String, Object> result = new LinkedHashMap<>();

        int currentYear = LocalDate.now().getYear();
        int recentYearsThreshold = 10;

        List<Map<String, Object>> emergingNations = new ArrayList<>();

        countryRepository.findAll().forEach(country -> {
            // Skip top-tier nations
            if (country.getOverallCapabilityScore() != null && country.getOverallCapabilityScore() > 70) {
                return;
            }

            Map<String, Object> nationData = new LinkedHashMap<>();
            nationData.put("countryId", country.getId());
            nationData.put("countryName", country.getName());
            nationData.put("isoCode", country.getIsoCode());
            nationData.put("currentScore", country.getOverallCapabilityScore());

            // Count recent missions
            List<SpaceMission> recentMissions = spaceMissionRepository.findByCountryIdOrderByLaunchDateDesc(country.getId())
                .stream()
                .filter(m -> m.getLaunchDate() != null && m.getLaunchDate().getYear() >= currentYear - recentYearsThreshold)
                .toList();
            nationData.put("recentMissions", recentMissions.size());

            // Count recent milestones
            List<SpaceMilestone> recentMilestones = spaceMilestoneRepository.findByCountryIdOrderByDateAchievedAsc(country.getId())
                .stream()
                .filter(m -> m.getDateAchieved() != null && m.getDateAchieved().getYear() >= currentYear - recentYearsThreshold)
                .toList();
            nationData.put("recentMilestones", recentMilestones.size());

            // Calculate emergence score (simple heuristic)
            int emergenceScore = recentMissions.size() * 5 + recentMilestones.size() * 10;
            if (country.getIndependentLaunchCapable() != null && country.getIndependentLaunchCapable()) {
                emergenceScore += 20;
            }
            nationData.put("emergenceScore", emergenceScore);

            // Only include if there's recent activity
            if (emergenceScore > 0) {
                emergingNations.add(nationData);
            }
        });

        // Sort by emergence score
        emergingNations.sort((a, b) -> Integer.compare(
            (Integer) b.get("emergenceScore"),
            (Integer) a.get("emergenceScore")
        ));

        result.put("emergingNations", emergingNations.stream().limit(10).toList());
        result.put("analysisWindow", recentYearsThreshold + " years");
        result.put("totalEmerging", emergingNations.size());

        return result;
    }

    // ==================== Technology Trends ====================

    /**
     * Analyze technology trends in propulsion and space systems
     */
    public Map<String, Object> getTechnologyTrends() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Propellant trends
        Map<String, Long> propellantUsage = new LinkedHashMap<>();
        engineRepository.findAll().forEach(engine -> {
            String propellant = engine.getPropellant() != null ? engine.getPropellant() : "Unknown";
            propellantUsage.merge(propellant, 1L, Long::sum);
        });
        result.put("propellantDistribution", propellantUsage);

        // Power cycle trends
        Map<String, Long> cycleUsage = new LinkedHashMap<>();
        engineRepository.findAll().forEach(engine -> {
            String cycle = engine.getPowerCycle() != null ? engine.getPowerCycle() : "Unknown";
            cycleUsage.merge(cycle, 1L, Long::sum);
        });
        result.put("powerCycleDistribution", cycleUsage);

        // Reusability trend
        long reusableCount = countryRepository.findByReusableRocketCapableTrue().size();
        long totalWithCapability = countryRepository.findByIndependentLaunchCapableTrue().size();
        result.put("reusabilityAdoption", Map.of(
            "countriesWithReusable", reusableCount,
            "countriesWithLaunchCapability", totalWithCapability,
            "adoptionRate", totalWithCapability > 0 ?
                Math.round((double) reusableCount / totalWithCapability * 10000.0) / 100.0 : 0
        ));

        // Satellite type trends
        result.put("satelliteTypeTrends", satelliteRepository.countSatellitesByType());

        // Orbit utilization
        result.put("orbitUtilization", satelliteRepository.countSatellitesByOrbit());

        // Launch site capability trends
        result.put("launchSiteCapabilities", Map.of(
            "total", launchSiteRepository.count(),
            "active", launchSiteRepository.countActiveLaunchSites(),
            "humanRated", launchSiteRepository.findHumanRatedSites().size(),
            "interplanetary", launchSiteRepository.findInterplanetaryCapableSites().size(),
            "withLanding", launchSiteRepository.findSitesWithLandingFacilities().size()
        ));

        return result;
    }

    // ==================== Records ====================

    /**
     * Get current world records in various categories
     */
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new LinkedHashMap<>();

        // Engine records
        Map<String, Object> engineRecords = new LinkedHashMap<>();

        // Highest thrust
        engineRepository.findAll().stream()
            .filter(e -> e.getThrustN() != null)
            .max(Comparator.comparing(Engine::getThrustN))
            .ifPresent(e -> engineRecords.put("highestThrust", Map.of(
                "engineId", e.getId(),
                "engineName", e.getName(),
                "thrustN", e.getThrustN(),
                "country", e.getCountry() != null ? e.getCountry().getName() : e.getOrigin()
            )));

        // Highest ISP
        engineRepository.findAll().stream()
            .filter(e -> e.getIsp_s() != null)
            .max(Comparator.comparing(Engine::getIsp_s))
            .ifPresent(e -> engineRecords.put("highestIsp", Map.of(
                "engineId", e.getId(),
                "engineName", e.getName(),
                "ispS", e.getIsp_s(),
                "country", e.getCountry() != null ? e.getCountry().getName() : e.getOrigin()
            )));

        records.put("engines", engineRecords);

        // Country records
        Map<String, Object> countryRecords = new LinkedHashMap<>();

        // Most launches
        countryRepository.findAll().stream()
            .filter(c -> c.getTotalLaunches() != null)
            .max(Comparator.comparing(Country::getTotalLaunches))
            .ifPresent(c -> countryRecords.put("mostLaunches", Map.of(
                "countryId", c.getId(),
                "countryName", c.getName(),
                "totalLaunches", c.getTotalLaunches()
            )));

        // Highest success rate (min 10 launches)
        countryRepository.findAll().stream()
            .filter(c -> c.getTotalLaunches() != null && c.getTotalLaunches() >= 10)
            .filter(c -> c.getLaunchSuccessRate() != null)
            .max(Comparator.comparing(Country::getLaunchSuccessRate))
            .ifPresent(c -> countryRecords.put("highestSuccessRate", Map.of(
                "countryId", c.getId(),
                "countryName", c.getName(),
                "successRate", c.getLaunchSuccessRate(),
                "totalLaunches", c.getTotalLaunches()
            )));

        // Highest capability score
        countryRepository.findAll().stream()
            .filter(c -> c.getOverallCapabilityScore() != null)
            .max(Comparator.comparing(Country::getOverallCapabilityScore))
            .ifPresent(c -> countryRecords.put("highestCapabilityScore", Map.of(
                "countryId", c.getId(),
                "countryName", c.getName(),
                "score", c.getOverallCapabilityScore()
            )));

        // Most global firsts
        Map<String, Long> firstsCount = spaceMilestoneRepository.findAllGlobalFirsts().stream()
            .filter(m -> m.getCountry() != null)
            .collect(Collectors.groupingBy(
                m -> m.getCountry().getName(),
                Collectors.counting()
            ));
        firstsCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> countryRecords.put("mostGlobalFirsts", Map.of(
                "countryName", entry.getKey(),
                "count", entry.getValue()
            )));

        records.put("countries", countryRecords);

        // Mission records
        Map<String, Object> missionRecords = new LinkedHashMap<>();

        // Longest mission
        spaceMissionRepository.findAll().stream()
            .filter(m -> m.getDurationDays() != null)
            .max(Comparator.comparing(SpaceMission::getDurationDays))
            .ifPresent(m -> missionRecords.put("longestMission", Map.of(
                "missionId", m.getId(),
                "missionName", m.getName(),
                "durationDays", m.getDurationDays(),
                "country", m.getCountry() != null ? m.getCountry().getName() : null
            )));

        // Largest crew
        spaceMissionRepository.findAll().stream()
            .filter(m -> m.getCrewSize() != null)
            .max(Comparator.comparing(SpaceMission::getCrewSize))
            .ifPresent(m -> missionRecords.put("largestCrew", Map.of(
                "missionId", m.getId(),
                "missionName", m.getName(),
                "crewSize", m.getCrewSize(),
                "country", m.getCountry() != null ? m.getCountry().getName() : null
            )));

        records.put("missions", missionRecords);

        // Satellite records
        Map<String, Object> satelliteRecords = new LinkedHashMap<>();

        // Largest constellation
        Map<String, Long> constellationSizes = satelliteRepository.findAll().stream()
            .filter(s -> s.getConstellation() != null && !s.getConstellation().isEmpty())
            .collect(Collectors.groupingBy(
                Satellite::getConstellation,
                Collectors.counting()
            ));
        constellationSizes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> satelliteRecords.put("largestConstellation", Map.of(
                "name", entry.getKey(),
                "count", entry.getValue()
            )));

        // Most satellites by country
        Map<String, Long> satellitesByCountry = new LinkedHashMap<>();
        countryRepository.findAll().forEach(c -> {
            Long count = satelliteRepository.countByCountry(c.getId());
            if (count > 0) {
                satellitesByCountry.put(c.getName(), count);
            }
        });
        satellitesByCountry.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> satelliteRecords.put("mostSatellites", Map.of(
                "countryName", entry.getKey(),
                "count", entry.getValue()
            )));

        records.put("satellites", satelliteRecords);

        return records;
    }

    // ==================== Summary Dashboard ====================

    /**
     * Get comprehensive analytics summary
     */
    public Map<String, Object> getAnalyticsSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        summary.put("launchTrends", getLaunchesPerYear());
        summary.put("technologyTrends", getTechnologyTrends());
        summary.put("records", getRecords());
        summary.put("generatedAt", LocalDate.now().toString());

        return summary;
    }
}
