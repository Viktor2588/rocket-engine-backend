package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.rocket.comparison.config.CacheConfig.*;
import static com.rocket.comparison.constants.SpaceConstants.*;

/**
 * Service providing analytics and trend analysis across space-related data.
 * Supports historical analysis, trend detection, and record tracking.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;
    private final CapabilityScoreRepository capabilityScoreRepository;

    // ==================== Budget Analytics ====================

    /**
     * Get space budget trends by country
     * Optimized: Uses database-level filtering and sorting
     */
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getBudgetTrends() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Use optimized query instead of loading all countries
        List<Country> countriesWithBudget = countryRepository.findCountriesWithBudgetOrderByBudgetDesc();

        List<Map<String, Object>> countryBudgets = countriesWithBudget.stream()
            .map(country -> {
                Map<String, Object> countryData = new LinkedHashMap<>();
                countryData.put("countryId", country.getId());
                countryData.put("countryName", country.getName());
                countryData.put("isoCode", country.getIsoCode());
                countryData.put("annualBudgetUsd", country.getAnnualBudgetUsd());
                countryData.put("budgetYear", CURRENT_BUDGET_YEAR);

                if (country.getOverallCapabilityScore() != null) {
                    countryData.put("capabilityScore", country.getOverallCapabilityScore());
                }

                countryData.put("totalLaunches", country.getTotalLaunches());
                countryData.put("successRate", country.getLaunchSuccessRate());

                return countryData;
            })
            .toList();

        result.put("countries", countryBudgets);
        result.put("totalCountries", countryBudgets.size());

        // Calculate global totals
        java.math.BigDecimal totalGlobalBudget = countryBudgets.stream()
            .map(c -> (java.math.BigDecimal) c.get("annualBudgetUsd"))
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        result.put("totalGlobalBudgetUsd", totalGlobalBudget);

        // Top by budget
        result.put("topByBudget", countryBudgets.stream().limit(DEFAULT_TOP_LIMIT).toList());

        return result;
    }

    // ==================== Launch Analytics ====================

    /**
     * Get global launches per year in frontend-compatible format
     * Format: { years: [], byCountry: {}, total: [] }
     * Uses real mission data from database synced from TheSpaceDevs API
     */
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getLaunchesPerYear() {
        Map<String, Object> result = new LinkedHashMap<>();

        int currentYear = LocalDate.now().getYear();
        int startYear = currentYear - 5;

        // Years array
        List<Integer> years = new ArrayList<>();
        for (int y = startYear; y <= currentYear; y++) {
            years.add(y);
        }
        result.put("years", years);

        // Get all missions with launch dates in our date range
        List<SpaceMission> missions = spaceMissionRepository.findByYearRange(startYear, currentYear);

        // Group missions by country and year
        Map<String, Map<Integer, Long>> countryYearCounts = missions.stream()
            .filter(m -> m.getLaunchDate() != null && m.getCountry() != null)
            .collect(Collectors.groupingBy(
                m -> m.getCountry().getName(),
                Collectors.groupingBy(
                    m -> m.getLaunchDate().getYear(),
                    Collectors.counting()
                )
            ));

        // Build byCountry arrays aligned with years
        Map<String, List<Long>> byCountry = new LinkedHashMap<>();
        List<Long> totals = new ArrayList<>(Collections.nCopies(years.size(), 0L));

        for (Map.Entry<String, Map<Integer, Long>> countryEntry : countryYearCounts.entrySet()) {
            String countryName = countryEntry.getKey();
            Map<Integer, Long> yearCounts = countryEntry.getValue();

            List<Long> countryData = new ArrayList<>();
            for (int i = 0; i < years.size(); i++) {
                int year = years.get(i);
                long count = yearCounts.getOrDefault(year, 0L);
                countryData.add(count);
                totals.set(i, totals.get(i) + count);
            }

            // Only include countries with at least 1 launch
            if (countryData.stream().mapToLong(Long::longValue).sum() > 0) {
                byCountry.put(countryName, countryData);
            }
        }

        // Sort by total launches descending
        byCountry = byCountry.entrySet().stream()
            .sorted((a, b) -> Long.compare(
                b.getValue().stream().mapToLong(Long::longValue).sum(),
                a.getValue().stream().mapToLong(Long::longValue).sum()
            ))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));

        result.put("byCountry", byCountry);
        result.put("total", totals);

        return result;
    }

    /**
     * Get launches per year broken down by country
     * BE-053: Optimized with single GROUP BY query instead of N+1 queries
     */
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getLaunchesPerYearByCountry() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Use optimized query to get all data in one go
        List<Object[]> rawData = spaceMissionRepository.countMissionsByCountryAndYear();

        // Group by country
        Map<Long, Map<String, Object>> countryMap = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            Long countryId = (Long) row[0];
            String countryName = (String) row[1];
            String isoCode = (String) row[2];
            Integer year = (Integer) row[3];
            Long count = (Long) row[4];

            countryMap.computeIfAbsent(countryId, k -> {
                Map<String, Object> countryStats = new LinkedHashMap<>();
                countryStats.put("countryId", countryId);
                countryStats.put("countryName", countryName);
                countryStats.put("isoCode", isoCode);
                countryStats.put("totalMissions", 0L);
                countryStats.put("byYear", new LinkedHashMap<Integer, Long>());
                return countryStats;
            });

            Map<String, Object> countryStats = countryMap.get(countryId);
            countryStats.put("totalMissions", (Long) countryStats.get("totalMissions") + count);
            @SuppressWarnings("unchecked")
            Map<Integer, Long> byYear = (Map<Integer, Long>) countryStats.get("byYear");
            byYear.put(year, count);
        }

        // Convert to list and sort by total missions descending
        List<Map<String, Object>> countryData = new ArrayList<>(countryMap.values());
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
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getEmergingNations() {
        Map<String, Object> result = new LinkedHashMap<>();

        int currentYear = LocalDate.now().getYear();

        List<Map<String, Object>> emergingNations = new ArrayList<>();

        countryRepository.findAll().forEach(country -> {
            // Skip top-tier nations
            if (country.getOverallCapabilityScore() != null && country.getOverallCapabilityScore() > TOP_TIER_SCORE_THRESHOLD) {
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
                .filter(m -> m.getLaunchDate() != null && m.getLaunchDate().getYear() >= currentYear - EMERGING_NATIONS_YEARS_WINDOW)
                .toList();
            nationData.put("recentMissions", recentMissions.size());

            // Count recent milestones
            List<SpaceMilestone> recentMilestones = spaceMilestoneRepository.findByCountryIdOrderByDateAchievedAsc(country.getId())
                .stream()
                .filter(m -> m.getDateAchieved() != null && m.getDateAchieved().getYear() >= currentYear - EMERGING_NATIONS_YEARS_WINDOW)
                .toList();
            nationData.put("recentMilestones", recentMilestones.size());

            // Calculate emergence score (simple heuristic)
            int emergenceScore = recentMissions.size() * EMERGENCE_POINTS_PER_MISSION + recentMilestones.size() * EMERGENCE_POINTS_PER_MILESTONE;
            if (country.getIndependentLaunchCapable() != null && country.getIndependentLaunchCapable()) {
                emergenceScore += EMERGENCE_LAUNCH_CAPABILITY_BONUS;
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

        result.put("emergingNations", emergingNations.stream().limit(EMERGING_NATIONS_LIMIT).toList());
        result.put("analysisWindow", EMERGING_NATIONS_YEARS_WINDOW + " years");
        result.put("totalEmerging", emergingNations.size());

        return result;
    }

    // ==================== Technology Trends ====================

    /**
     * Analyze technology trends in propulsion and space systems
     * Optimized: Uses database-level aggregation instead of loading all entities
     */
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getTechnologyTrends() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Propellant trends - optimized with database aggregation
        Map<String, Long> propellantUsage = new LinkedHashMap<>();
        engineRepository.countByPropellant().forEach(row -> {
            String propellant = row[0] != null ? (String) row[0] : "Unknown";
            Long count = (Long) row[1];
            propellantUsage.put(propellant, count);
        });
        result.put("propellantDistribution", propellantUsage);

        // Power cycle trends - optimized with database aggregation
        Map<String, Long> cycleUsage = new LinkedHashMap<>();
        engineRepository.countByPowerCycle().forEach(row -> {
            String cycle = row[0] != null ? (String) row[0] : "Unknown";
            Long count = (Long) row[1];
            cycleUsage.put(cycle, count);
        });
        result.put("powerCycleDistribution", cycleUsage);

        // Reusability trend - BE-052: Use COUNT queries instead of findAll().size()
        long reusableCount = countryRepository.countWithReusableCapability();
        long totalWithCapability = countryRepository.countWithLaunchCapability();
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

        // Launch site capability trends - BE-052: Use COUNT queries
        result.put("launchSiteCapabilities", Map.of(
            "total", launchSiteRepository.count(),
            "active", launchSiteRepository.countActiveLaunchSites(),
            "humanRated", launchSiteRepository.countHumanRatedSites(),
            "interplanetary", launchSiteRepository.countInterplanetaryCapableSites(),
            "withLanding", launchSiteRepository.countSitesWithLandingFacilities()
        ));

        return result;
    }

    // ==================== Records ====================

    /**
     * Get current world records in various categories
     */
    @Cacheable(RANKINGS_CACHE)
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new LinkedHashMap<>();

        // Engine records - optimized with direct database queries
        Map<String, Object> engineRecords = new LinkedHashMap<>();

        // Highest thrust - optimized query
        engineRepository.findEngineWithHighestThrust().stream()
            .findFirst()
            .ifPresent(e -> engineRecords.put("highestThrust", Map.of(
                "engineId", e.getId(),
                "engineName", e.getName(),
                "thrustN", e.getThrustN(),
                "country", e.getCountry() != null ? e.getCountry().getName() : e.getOrigin()
            )));

        // Highest ISP - optimized query
        engineRepository.findEngineWithHighestIsp().stream()
            .findFirst()
            .ifPresent(e -> engineRecords.put("highestIsp", Map.of(
                "engineId", e.getId(),
                "engineName", e.getName(),
                "ispS", e.getIsp_s(),
                "country", e.getCountry() != null ? e.getCountry().getName() : e.getOrigin()
            )));

        records.put("engines", engineRecords);

        // Country records - BE-052: Use LIMIT 1 queries instead of findAll() streams
        Map<String, Object> countryRecords = new LinkedHashMap<>();

        // Most launches - optimized query
        countryRepository.findCountryWithMostLaunches()
            .ifPresent(c -> countryRecords.put("mostLaunches", Map.of(
                "countryId", c.getId(),
                "countryName", c.getName(),
                "totalLaunches", c.getTotalLaunches()
            )));

        // Highest success rate (min launches required) - optimized query
        countryRepository.findCountryWithHighestSuccessRate(MIN_LAUNCHES_FOR_RANKING)
            .ifPresent(c -> countryRecords.put("highestSuccessRate", Map.of(
                "countryId", c.getId(),
                "countryName", c.getName(),
                "successRate", c.getLaunchSuccessRate(),
                "totalLaunches", c.getTotalLaunches()
            )));

        // Highest capability score - optimized query
        countryRepository.findCountryWithHighestCapabilityScore()
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

        // Mission records - BE-052: Use ORDER BY LIMIT queries instead of findAll() streams
        Map<String, Object> missionRecords = new LinkedHashMap<>();

        // Longest mission - use optimized query
        spaceMissionRepository.findLongestMissions().stream()
            .findFirst()
            .ifPresent(m -> missionRecords.put("longestMission", Map.of(
                "missionId", m.getId(),
                "missionName", m.getName(),
                "durationDays", m.getDurationDays(),
                "country", m.getCountry() != null ? m.getCountry().getName() : null
            )));

        // Largest crew - use optimized query
        spaceMissionRepository.findLargestCrewMissions().stream()
            .findFirst()
            .ifPresent(m -> missionRecords.put("largestCrew", Map.of(
                "missionId", m.getId(),
                "missionName", m.getName(),
                "crewSize", m.getCrewSize(),
                "country", m.getCountry() != null ? m.getCountry().getName() : null
            )));

        records.put("missions", missionRecords);

        // Satellite records - BE-052: Use GROUP BY queries instead of findAll() streams
        Map<String, Object> satelliteRecords = new LinkedHashMap<>();

        // Largest constellation - use GROUP BY query
        satelliteRepository.countSatellitesByConstellation().stream()
            .findFirst()
            .ifPresent(row -> {
                String name = (String) row[0];
                Long count = (Long) row[1];
                if (name != null) {
                    satelliteRecords.put("largestConstellation", Map.of(
                        "name", name,
                        "count", count
                    ));
                }
            });

        // Most satellites by country - use GROUP BY query
        satelliteRepository.countSatellitesByCountry().stream()
            .findFirst()
            .ifPresent(row -> {
                Long countryId = (Long) row[0];
                Long count = (Long) row[1];
                countryRepository.findById(countryId).ifPresent(country ->
                    satelliteRecords.put("mostSatellites", Map.of(
                        "countryName", country.getName(),
                        "countryId", countryId,
                        "count", count
                    ))
                );
            });

        records.put("satellites", satelliteRecords);

        return records;
    }

    // ==================== Summary Dashboard ====================

    /**
     * Get comprehensive analytics summary
     */
    @Cacheable(ANALYTICS_CACHE)
    public Map<String, Object> getAnalyticsSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        summary.put("launchTrends", getLaunchesPerYear());
        summary.put("technologyTrends", getTechnologyTrends());
        summary.put("records", getRecords());
        summary.put("generatedAt", LocalDate.now().toString());

        return summary;
    }
}
