package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service providing aggregated data optimized for frontend visualizations.
 * Supports world maps, charts, timelines, and comparison views.
 */
@Service
@RequiredArgsConstructor
public class VisualizationService {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final SpaceMissionRepository missionRepository;
    private final SpaceMilestoneRepository milestoneRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final CapabilityScoreRepository scoreRepository;

    // ==================== World Map Data ====================

    /**
     * Get country data for world map visualization with capability scores
     */
    public List<Map<String, Object>> getWorldMapData() {
        List<Country> countries = countryRepository.findAll();
        List<Map<String, Object>> mapData = new ArrayList<>();

        for (Country country : countries) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", country.getId());
            data.put("name", country.getName());
            data.put("isoCode", country.getIsoCode());
            data.put("flagUrl", country.getFlagUrl());
            data.put("region", country.getRegion());

            // Capability score
            Double score = country.getOverallCapabilityScore();
            data.put("capabilityScore", score != null ? score : 0.0);

            // Capability flags
            data.put("humanSpaceflightCapable", country.getHumanSpaceflightCapable());
            data.put("independentLaunchCapable", country.getIndependentLaunchCapable());
            data.put("reusableRocketCapable", country.getReusableRocketCapable());
            data.put("deepSpaceCapable", country.getDeepSpaceCapable());

            // Quick stats
            data.put("totalLaunches", country.getTotalLaunches());
            data.put("activeAstronauts", country.getActiveAstronauts());

            mapData.add(data);
        }

        return mapData;
    }

    /**
     * Get launch sites for map markers
     */
    public List<Map<String, Object>> getLaunchSiteMapData() {
        return launchSiteRepository.findAll().stream()
                .filter(site -> site.getLatitude() != null && site.getLongitude() != null)
                .map(site -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", site.getId());
                    data.put("name", site.getName());
                    data.put("shortName", site.getShortName());
                    data.put("latitude", site.getLatitude());
                    data.put("longitude", site.getLongitude());
                    data.put("country", site.getCountry().getName());
                    data.put("countryCode", site.getCountry().getIsoCode());
                    data.put("status", site.getStatus().getDisplayName());
                    data.put("isActive", site.getStatus().isActive());
                    data.put("totalLaunches", site.getTotalLaunches());
                    data.put("humanRated", site.getHumanRatedCapable());
                    return data;
                })
                .toList();
    }

    // ==================== Timeline Data ====================

    /**
     * Get milestone timeline data for visualization
     */
    public Map<String, Object> getMilestoneTimeline(Integer startYear, Integer endYear) {
        if (startYear == null) startYear = 1957;
        if (endYear == null) endYear = LocalDate.now().getYear();

        List<SpaceMilestone> milestones = milestoneRepository.findByYearRange(startYear, endYear);

        Map<String, Object> timeline = new HashMap<>();
        timeline.put("startYear", startYear);
        timeline.put("endYear", endYear);

        // Group by year
        Map<Integer, List<Map<String, Object>>> byYear = milestones.stream()
                .filter(m -> m.getYear() != null)
                .collect(Collectors.groupingBy(
                        SpaceMilestone::getYear,
                        TreeMap::new,
                        Collectors.mapping(this::milestoneToTimelineItem, Collectors.toList())
                ));
        timeline.put("byYear", byYear);

        // Group by country
        Map<String, List<Map<String, Object>>> byCountry = milestones.stream()
                .filter(m -> m.getCountry() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getCountry().getName(),
                        Collectors.mapping(this::milestoneToTimelineItem, Collectors.toList())
                ));
        timeline.put("byCountry", byCountry);

        // Summary stats
        timeline.put("totalMilestones", milestones.size());
        timeline.put("globalFirsts", milestones.stream().filter(m -> Boolean.TRUE.equals(m.getIsGlobalFirst())).count());

        return timeline;
    }

    private Map<String, Object> milestoneToTimelineItem(SpaceMilestone milestone) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", milestone.getId());
        item.put("title", milestone.getTitle());
        item.put("date", milestone.getDateAchieved());
        item.put("year", milestone.getYear());
        item.put("type", milestone.getMilestoneType().getDisplayName());
        item.put("category", milestone.getMilestoneType().getCategory());
        item.put("country", milestone.getCountry().getName());
        item.put("countryCode", milestone.getCountry().getIsoCode());
        item.put("isGlobalFirst", milestone.getIsGlobalFirst());
        item.put("globalRank", milestone.getGlobalRank());
        item.put("achievedBy", milestone.getAchievedBy());
        item.put("era", milestone.getEra());
        return item;
    }

    /**
     * Get mission timeline for visualization
     */
    public Map<String, Object> getMissionTimeline(Integer startYear, Integer endYear) {
        if (startYear == null) startYear = 1957;
        if (endYear == null) endYear = LocalDate.now().getYear();

        List<SpaceMission> missions = missionRepository.findByYearRange(startYear, endYear);

        Map<String, Object> timeline = new HashMap<>();
        timeline.put("startYear", startYear);
        timeline.put("endYear", endYear);

        // Counts by year
        Map<Integer, Long> countsByYear = missions.stream()
                .filter(m -> m.getLaunchYear() != null)
                .collect(Collectors.groupingBy(
                        SpaceMission::getLaunchYear,
                        TreeMap::new,
                        Collectors.counting()
                ));
        timeline.put("countsByYear", countsByYear);

        // By country by year (for stacked chart)
        Map<String, Map<Integer, Long>> byCountryByYear = missions.stream()
                .filter(m -> m.getLaunchYear() != null && m.getCountry() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getCountry().getName(),
                        Collectors.groupingBy(
                                SpaceMission::getLaunchYear,
                                TreeMap::new,
                                Collectors.counting()
                        )
                ));
        timeline.put("byCountryByYear", byCountryByYear);

        // By type by year
        Map<String, Map<Integer, Long>> byTypeByYear = missions.stream()
                .filter(m -> m.getLaunchYear() != null && m.getMissionType() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getMissionType().getCategory(),
                        Collectors.groupingBy(
                                SpaceMission::getLaunchYear,
                                TreeMap::new,
                                Collectors.counting()
                        )
                ));
        timeline.put("byTypeByYear", byTypeByYear);

        return timeline;
    }

    // ==================== Chart Data ====================

    /**
     * Get radar chart data for country capability comparison
     */
    public Map<String, Object> getCapabilityRadarData(List<Long> countryIds) {
        Map<String, Object> radarData = new HashMap<>();

        List<String> categories = Arrays.stream(CapabilityCategory.values())
                .map(CapabilityCategory::getDisplayName)
                .toList();
        radarData.put("categories", categories);

        List<Map<String, Object>> datasets = new ArrayList<>();
        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId).orElse(null);
            if (country == null) continue;

            Map<String, Object> dataset = new HashMap<>();
            dataset.put("countryId", countryId);
            dataset.put("countryName", country.getName());
            dataset.put("isoCode", country.getIsoCode());

            List<CapabilityScore> scores = scoreRepository.findByCountryId(countryId);
            List<Double> values = new ArrayList<>();
            for (CapabilityCategory category : CapabilityCategory.values()) {
                double score = scores.stream()
                        .filter(s -> s.getCategory() == category)
                        .findFirst()
                        .map(CapabilityScore::getScore)
                        .orElse(0.0);
                values.add(score);
            }
            dataset.put("values", values);
            datasets.add(dataset);
        }
        radarData.put("datasets", datasets);

        return radarData;
    }

    /**
     * Get engine comparison bubble chart data (thrust vs ISP vs country)
     */
    public List<Map<String, Object>> getEngineBubbleChartData() {
        return engineRepository.findAll().stream()
                .filter(e -> e.getThrustN() != null && e.getIsp_s() != null)
                .map(engine -> {
                    Map<String, Object> point = new HashMap<>();
                    point.put("id", engine.getId());
                    point.put("name", engine.getName());
                    point.put("x", engine.getThrustN() / 1000.0); // Thrust in kN on X-axis
                    point.put("y", engine.getIsp_s()); // ISP on Y-axis
                    point.put("r", Math.log10(engine.getThrustN() + 1) * 3); // Bubble size based on thrust
                    point.put("country", engine.getCountry() != null ? engine.getCountry().getName() : engine.getOrigin());
                    point.put("countryCode", engine.getCountry() != null ? engine.getCountry().getIsoCode() : null);
                    point.put("cycle", engine.getPowerCycle());
                    point.put("propellant", engine.getPropellant());
                    point.put("status", engine.getStatus());
                    return point;
                })
                .toList();
    }

    /**
     * Get satellite distribution treemap data
     */
    public Map<String, Object> getSatelliteTreemapData() {
        Map<String, Object> treemap = new HashMap<>();

        // By type
        List<Object[]> byType = satelliteRepository.countSatellitesByType();
        List<Map<String, Object>> typeNodes = byType.stream()
                .map(row -> {
                    SatelliteType type = (SatelliteType) row[0];
                    Long count = (Long) row[1];
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", type.getDisplayName());
                    node.put("category", type.getCategory());
                    node.put("value", count);
                    return node;
                })
                .toList();
        treemap.put("byType", typeNodes);

        // By country
        List<Object[]> byCountry = satelliteRepository.countSatellitesByCountry();
        List<Map<String, Object>> countryNodes = new ArrayList<>();
        for (Object[] row : byCountry) {
            Long countryId = (Long) row[0];
            Long count = (Long) row[1];
            countryRepository.findById(countryId).ifPresent(country -> {
                Map<String, Object> node = new HashMap<>();
                node.put("name", country.getName());
                node.put("isoCode", country.getIsoCode());
                node.put("value", count);
                countryNodes.add(node);
            });
        }
        treemap.put("byCountry", countryNodes);

        // By constellation
        List<Object[]> byConstellation = satelliteRepository.countSatellitesByConstellation();
        List<Map<String, Object>> constellationNodes = byConstellation.stream()
                .map(row -> {
                    String constellation = (String) row[0];
                    Long count = (Long) row[1];
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", constellation);
                    node.put("value", count);
                    return node;
                })
                .toList();
        treemap.put("byConstellation", constellationNodes);

        return treemap;
    }

    /**
     * Get launch frequency stacked bar chart data
     */
    public Map<String, Object> getLaunchFrequencyChartData(Integer startYear, Integer endYear) {
        if (startYear == null) startYear = 2000;
        if (endYear == null) endYear = LocalDate.now().getYear();

        List<SpaceMission> missions = missionRepository.findByYearRange(startYear, endYear);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("startYear", startYear);
        chartData.put("endYear", endYear);

        // Get all years in range
        List<Integer> years = new ArrayList<>();
        for (int y = startYear; y <= endYear; y++) {
            years.add(y);
        }
        chartData.put("labels", years);

        // Group by country
        Map<String, Map<Integer, Long>> byCountry = missions.stream()
                .filter(m -> m.getLaunchYear() != null && m.getCountry() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getCountry().getName(),
                        Collectors.groupingBy(
                                SpaceMission::getLaunchYear,
                                Collectors.counting()
                        )
                ));

        // Convert to datasets format
        List<Map<String, Object>> datasets = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Long>> entry : byCountry.entrySet()) {
            Map<String, Object> dataset = new HashMap<>();
            dataset.put("label", entry.getKey());
            List<Long> data = years.stream()
                    .map(year -> entry.getValue().getOrDefault(year, 0L))
                    .toList();
            dataset.put("data", data);
            datasets.add(dataset);
        }
        chartData.put("datasets", datasets);

        return chartData;
    }

    // ==================== Rankings Data ====================

    /**
     * Get country rankings for leaderboard visualization
     */
    public List<Map<String, Object>> getCountryRankings() {
        List<Country> countries = countryRepository.findAllOrderByCapabilityScoreDesc();

        List<Map<String, Object>> rankings = new ArrayList<>();
        int rank = 1;
        for (Country country : countries) {
            if (country.getOverallCapabilityScore() == null || country.getOverallCapabilityScore() == 0) continue;

            Map<String, Object> entry = new HashMap<>();
            entry.put("rank", rank++);
            entry.put("id", country.getId());
            entry.put("name", country.getName());
            entry.put("isoCode", country.getIsoCode());
            entry.put("flagUrl", country.getFlagUrl());
            entry.put("score", country.getOverallCapabilityScore());
            entry.put("spaceAgency", country.getSpaceAgencyAcronym());
            entry.put("totalLaunches", country.getTotalLaunches());
            entry.put("humanSpaceflight", country.getHumanSpaceflightCapable());
            entry.put("deepSpace", country.getDeepSpaceCapable());
            rankings.add(entry);
        }

        return rankings;
    }

    /**
     * Get "firsts" leaderboard - which countries achieved the most historic firsts
     */
    public List<Map<String, Object>> getFirstsLeaderboard() {
        List<Object[]> results = milestoneRepository.countFirstsByCountryRanked();

        List<Map<String, Object>> leaderboard = new ArrayList<>();
        int rank = 1;
        for (Object[] row : results) {
            Long countryId = (Long) row[0];
            Long count = (Long) row[1];

            countryRepository.findById(countryId).ifPresent(country -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("rank", leaderboard.size() + 1);
                entry.put("countryId", countryId);
                entry.put("countryName", country.getName());
                entry.put("isoCode", country.getIsoCode());
                entry.put("flagUrl", country.getFlagUrl());
                entry.put("firstsCount", count);
                leaderboard.add(entry);
            });
        }

        return leaderboard;
    }

    // ==================== Dashboard Summary ====================

    /**
     * Get dashboard summary data
     */
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Counts
        summary.put("totalCountries", countryRepository.count());
        summary.put("totalEngines", engineRepository.count());
        summary.put("totalMissions", missionRepository.count());
        summary.put("totalMilestones", milestoneRepository.count());
        summary.put("totalSatellites", satelliteRepository.count());
        summary.put("totalLaunchSites", launchSiteRepository.count());

        // Active counts
        summary.put("activeSatellites", satelliteRepository.countActiveSatellites());
        summary.put("activeLaunchSites", launchSiteRepository.countActiveLaunchSites());
        summary.put("activeMissions", missionRepository.findActiveMissions().size());

        // Capability counts
        summary.put("countriesWithLaunchCapability", countryRepository.findByIndependentLaunchCapableTrue().size());
        summary.put("countriesWithHumanSpaceflight", countryRepository.findByHumanSpaceflightCapableTrue().size());
        summary.put("countriesWithDeepSpace", countryRepository.findByDeepSpaceCapableTrue().size());

        // Top country
        List<Country> topCountries = countryRepository.findAllOrderByCapabilityScoreDesc();
        if (!topCountries.isEmpty() && topCountries.get(0).getOverallCapabilityScore() != null) {
            Country top = topCountries.get(0);
            summary.put("topCountry", Map.of(
                    "name", top.getName(),
                    "isoCode", top.getIsoCode(),
                    "score", top.getOverallCapabilityScore()
            ));
        }

        // Recent activity (missions in last year)
        int currentYear = LocalDate.now().getYear();
        summary.put("missionsThisYear", missionRepository.findByLaunchYear(currentYear).size());

        return summary;
    }
}
