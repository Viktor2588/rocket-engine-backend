package com.rocket.comparison.service;

import com.rocket.comparison.constants.SpaceConstants;
import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.rocket.comparison.config.CacheConfig.*;
import static com.rocket.comparison.constants.SpaceConstants.*;

/**
 * Service providing advanced side-by-side comparison capabilities.
 * Enables detailed comparisons between countries, engines, satellites, and more.
 */
@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;

    // ==================== Country Comparisons ====================

    /**
     * Compare multiple countries across all dimensions
     */
    public Map<String, Object> compareCountries(List<Long> countryIds) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Country> countries = countryIds.stream()
            .map(id -> countryRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        if (countries.isEmpty()) {
            return Map.of("error", "No valid countries found");
        }

        // Basic info
        List<Map<String, Object>> countryInfo = countries.stream()
            .map(this::buildCountryBasicInfo)
            .toList();
        comparison.put("countries", countryInfo);

        // Capability comparison
        List<Map<String, Object>> capabilities = countries.stream()
            .map(this::buildCountryCapabilities)
            .toList();
        comparison.put("capabilities", capabilities);

        // Asset counts comparison
        List<Map<String, Object>> assets = countries.stream()
            .map(this::buildCountryAssets)
            .toList();
        comparison.put("assets", assets);

        // Rankings
        comparison.put("rankings", calculateCountryRankings(countries));

        return comparison;
    }

    /**
     * Get head-to-head comparison between two countries
     */
    public Map<String, Object> headToHeadCountryComparison(Long countryId1, Long countryId2) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        Optional<Country> country1Opt = countryRepository.findById(countryId1);
        Optional<Country> country2Opt = countryRepository.findById(countryId2);

        if (country1Opt.isEmpty() || country2Opt.isEmpty()) {
            return Map.of("error", "One or both countries not found");
        }

        Country country1 = country1Opt.get();
        Country country2 = country2Opt.get();

        comparison.put("country1", buildDetailedCountryProfile(country1));
        comparison.put("country2", buildDetailedCountryProfile(country2));

        // Winner determination for each category
        comparison.put("categoryWinners", determineCountryWinners(country1, country2));

        return comparison;
    }

    // ==================== Engine Comparisons ====================

    /**
     * Compare multiple engines
     */
    public Map<String, Object> compareEngines(List<Long> engineIds) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Engine> engines = engineIds.stream()
            .map(id -> engineRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        if (engines.isEmpty()) {
            return Map.of("error", "No valid engines found");
        }

        // Basic specs
        List<Map<String, Object>> specs = engines.stream()
            .map(this::buildEngineSpecs)
            .toList();
        comparison.put("engines", specs);

        // Performance comparison
        comparison.put("performanceComparison", buildEnginePerformanceComparison(engines));

        // Rankings
        comparison.put("rankings", calculateEngineRankings(engines));

        return comparison;
    }

    /**
     * Find similar engines to a given engine
     */
    public List<Map<String, Object>> findSimilarEngines(Long engineId, int limit) {
        Optional<Engine> engineOpt = engineRepository.findById(engineId);
        if (engineOpt.isEmpty()) {
            return List.of();
        }

        Engine referenceEngine = engineOpt.get();
        List<Map<String, Object>> similar = new ArrayList<>();

        engineRepository.findAll().stream()
            .filter(e -> !e.getId().equals(engineId))
            .map(e -> {
                double similarity = calculateEngineSimilarity(referenceEngine, e);
                Map<String, Object> result = buildEngineSpecs(e);
                result.put("similarityScore", Math.round(similarity * 100.0) / 100.0);
                return result;
            })
            .sorted((a, b) -> Double.compare(
                (Double) b.get("similarityScore"),
                (Double) a.get("similarityScore")))
            .limit(limit)
            .forEach(similar::add);

        return similar;
    }

    // ==================== Satellite Comparisons ====================

    /**
     * Compare multiple satellites
     */
    public Map<String, Object> compareSatellites(List<Long> satelliteIds) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Satellite> satellites = satelliteIds.stream()
            .map(id -> satelliteRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        if (satellites.isEmpty()) {
            return Map.of("error", "No valid satellites found");
        }

        // Basic info
        List<Map<String, Object>> info = satellites.stream()
            .map(this::buildSatelliteInfo)
            .toList();
        comparison.put("satellites", info);

        // Orbital comparison
        comparison.put("orbitalComparison", buildOrbitalComparison(satellites));

        return comparison;
    }

    /**
     * Compare satellite constellations
     */
    public Map<String, Object> compareConstellations(List<String> constellationNames) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Map<String, Object>> constellations = constellationNames.stream()
            .map(name -> {
                List<Satellite> sats = satelliteRepository.findByConstellationOrderByLaunchDateDesc(name);
                if (sats.isEmpty()) return null;

                Map<String, Object> info = new LinkedHashMap<>();
                info.put("name", name);
                info.put("satelliteCount", sats.size());
                info.put("operationalCount", sats.stream()
                    .filter(s -> s.getStatus() == SatelliteStatus.OPERATIONAL)
                    .count());

                // Average altitude
                double avgAlt = sats.stream()
                    .filter(s -> s.getAltitudeKm() != null)
                    .mapToDouble(Satellite::getAltitudeKm)
                    .average()
                    .orElse(0);
                info.put("averageAltitudeKm", Math.round(avgAlt));

                return info;
            })
            .filter(Objects::nonNull)
            .toList();

        comparison.put("constellations", constellations);

        return comparison;
    }

    // ==================== Launch Site Comparisons ====================

    /**
     * Compare multiple launch sites
     */
    public Map<String, Object> compareLaunchSites(List<Long> siteIds) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<LaunchSite> sites = siteIds.stream()
            .map(id -> launchSiteRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        if (sites.isEmpty()) {
            return Map.of("error", "No valid launch sites found");
        }

        // Basic info
        List<Map<String, Object>> info = sites.stream()
            .map(this::buildLaunchSiteInfo)
            .toList();
        comparison.put("launchSites", info);

        // Capability comparison
        comparison.put("capabilityComparison", buildLaunchSiteCapabilityComparison(sites));

        // Location comparison
        comparison.put("locationComparison", buildLocationComparison(sites));

        return comparison;
    }

    // ==================== Cross-Entity Comparisons ====================

    /**
     * Compare technology levels across countries
     */
    public Map<String, Object> compareTechnologyLevels(List<Long> countryIds) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Country> countries = countryIds.stream()
            .map(id -> countryRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        List<Map<String, Object>> techLevels = countries.stream()
            .map(country -> {
                Map<String, Object> tech = new LinkedHashMap<>();
                tech.put("countryId", country.getId());
                tech.put("countryName", country.getName());

                // Engine count
                tech.put("engineCount", engineRepository.countByCountryId(country.getId()));

                // Satellite count
                tech.put("satelliteCount", satelliteRepository.countByCountry(country.getId()));

                // Launch site count
                tech.put("launchSiteCount", launchSiteRepository.countByCountry(country.getId()));

                // Capability score
                tech.put("capabilityScore", country.getOverallCapabilityScore());

                return tech;
            })
            .toList();

        comparison.put("technologyLevels", techLevels);

        return comparison;
    }

    // ==================== Gap Analysis (Milestone 8) ====================

    /**
     * Analyze the gap between two countries across all dimensions
     */
    public Map<String, Object> analyzeGap(Long country1Id, Long country2Id) {
        Map<String, Object> analysis = new LinkedHashMap<>();

        Optional<Country> country1Opt = countryRepository.findById(country1Id);
        Optional<Country> country2Opt = countryRepository.findById(country2Id);

        if (country1Opt.isEmpty() || country2Opt.isEmpty()) {
            return Map.of("error", "One or both countries not found");
        }

        Country c1 = country1Opt.get();
        Country c2 = country2Opt.get();

        analysis.put("country1", buildCountryBasicInfo(c1));
        analysis.put("country2", buildCountryBasicInfo(c2));

        // Capability gaps
        Map<String, Object> capabilityGaps = new LinkedHashMap<>();

        Double score1 = c1.getOverallCapabilityScore() != null ? c1.getOverallCapabilityScore() : 0.0;
        Double score2 = c2.getOverallCapabilityScore() != null ? c2.getOverallCapabilityScore() : 0.0;
        capabilityGaps.put("overallScoreGap", Math.abs(score1 - score2));
        capabilityGaps.put("leader", score1 > score2 ? c1.getName() : c2.getName());

        analysis.put("capabilityGaps", capabilityGaps);

        // Technology gaps
        Map<String, Object> techGaps = new LinkedHashMap<>();

        boolean launch1 = Boolean.TRUE.equals(c1.getIndependentLaunchCapable());
        boolean launch2 = Boolean.TRUE.equals(c2.getIndependentLaunchCapable());
        techGaps.put("launchCapabilityGap", launch1 != launch2 ?
            (launch1 ? c1.getName() + " has independent launch, " + c2.getName() + " does not" :
                      c2.getName() + " has independent launch, " + c1.getName() + " does not") : "Equal");

        boolean human1 = Boolean.TRUE.equals(c1.getHumanSpaceflightCapable());
        boolean human2 = Boolean.TRUE.equals(c2.getHumanSpaceflightCapable());
        techGaps.put("humanSpaceflightGap", human1 != human2 ?
            (human1 ? c1.getName() + " has human spaceflight, " + c2.getName() + " does not" :
                      c2.getName() + " has human spaceflight, " + c1.getName() + " does not") : "Equal");

        boolean reusable1 = Boolean.TRUE.equals(c1.getReusableRocketCapable());
        boolean reusable2 = Boolean.TRUE.equals(c2.getReusableRocketCapable());
        techGaps.put("reusableRocketGap", reusable1 != reusable2 ?
            (reusable1 ? c1.getName() + " has reusable rockets, " + c2.getName() + " does not" :
                        c2.getName() + " has reusable rockets, " + c1.getName() + " does not") : "Equal");

        analysis.put("technologyGaps", techGaps);

        // Asset gaps
        Map<String, Object> assetGaps = new LinkedHashMap<>();

        Long engines1 = engineRepository.countByCountryId(c1.getId());
        Long engines2 = engineRepository.countByCountryId(c2.getId());
        assetGaps.put("engineCountGap", Math.abs(engines1 - engines2));
        assetGaps.put("engineLeader", engines1 > engines2 ? c1.getName() : c2.getName());

        Long sats1 = satelliteRepository.countByCountry(c1.getId());
        Long sats2 = satelliteRepository.countByCountry(c2.getId());
        assetGaps.put("satelliteCountGap", Math.abs(sats1 - sats2));
        assetGaps.put("satelliteLeader", sats1 > sats2 ? c1.getName() : c2.getName());

        Long sites1 = launchSiteRepository.countByCountry(c1.getId());
        Long sites2 = launchSiteRepository.countByCountry(c2.getId());
        assetGaps.put("launchSiteCountGap", Math.abs(sites1 - sites2));
        assetGaps.put("launchSiteLeader", sites1 > sites2 ? c1.getName() : c2.getName());

        Long missions1 = spaceMissionRepository.countByCountry(c1.getId());
        Long missions2 = spaceMissionRepository.countByCountry(c2.getId());
        assetGaps.put("missionCountGap", Math.abs(missions1 - missions2));
        assetGaps.put("missionLeader", missions1 > missions2 ? c1.getName() : c2.getName());

        analysis.put("assetGaps", assetGaps);

        // Investment gaps
        Map<String, Object> investmentGaps = new LinkedHashMap<>();

        BigDecimal budget1 = c1.getAnnualBudgetUsd() != null ? c1.getAnnualBudgetUsd() : BigDecimal.ZERO;
        BigDecimal budget2 = c2.getAnnualBudgetUsd() != null ? c2.getAnnualBudgetUsd() : BigDecimal.ZERO;
        investmentGaps.put("budgetGapUsd", budget1.subtract(budget2).abs());
        investmentGaps.put("budgetLeader", budget1.compareTo(budget2) > 0 ? c1.getName() : c2.getName());

        Integer employees1 = c1.getTotalSpaceAgencyEmployees() != null ? c1.getTotalSpaceAgencyEmployees() : 0;
        Integer employees2 = c2.getTotalSpaceAgencyEmployees() != null ? c2.getTotalSpaceAgencyEmployees() : 0;
        investmentGaps.put("employeeCountGap", Math.abs(employees1 - employees2));
        investmentGaps.put("employeeLeader", employees1 > employees2 ? c1.getName() : c2.getName());

        analysis.put("investmentGaps", investmentGaps);

        // Summary with recommendations
        List<String> recommendations = generateGapRecommendations(c1, c2);
        analysis.put("recommendations", recommendations);

        return analysis;
    }

    /**
     * SWOT-style analysis for a single country
     */
    public Map<String, Object> analyzeStrengthsWeaknesses(Long countryId) {
        Map<String, Object> analysis = new LinkedHashMap<>();

        Optional<Country> countryOpt = countryRepository.findById(countryId);
        if (countryOpt.isEmpty()) {
            return Map.of("error", "Country not found");
        }

        Country country = countryOpt.get();
        analysis.put("country", buildCountryBasicInfo(country));

        // Strengths
        List<String> strengths = new ArrayList<>();

        if (Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            strengths.add("Independent launch capability");
        }
        if (Boolean.TRUE.equals(country.getHumanSpaceflightCapable())) {
            strengths.add("Human spaceflight capability");
        }
        if (Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            strengths.add("Reusable rocket technology");
        }
        if (Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            strengths.add("Deep space exploration capability");
        }
        if (Boolean.TRUE.equals(country.getSpaceStationCapable())) {
            strengths.add("Space station capability");
        }
        if (Boolean.TRUE.equals(country.getLunarLandingCapable())) {
            strengths.add("Lunar landing capability");
        }
        if (Boolean.TRUE.equals(country.getMarsLandingCapable())) {
            strengths.add("Mars landing capability");
        }

        if (country.getOverallCapabilityScore() != null && country.getOverallCapabilityScore() > TOP_TIER_SCORE_THRESHOLD) {
            strengths.add("High overall capability score (" + country.getOverallCapabilityScore() + ")");
        }

        if (country.getLaunchSuccessRate() != null && country.getLaunchSuccessRate() > EXCELLENT_SUCCESS_RATE) {
            strengths.add("Excellent launch success rate (" + country.getLaunchSuccessRate() + "%)");
        }

        Long engineCount = engineRepository.countByCountryId(countryId);
        if (engineCount > STRONG_ENGINE_PROGRAM_THRESHOLD) {
            strengths.add("Strong engine development program (" + engineCount + " engines)");
        }

        Long satelliteCount = satelliteRepository.countByCountry(countryId);
        if (satelliteCount > LARGE_SATELLITE_FLEET_THRESHOLD) {
            strengths.add("Large satellite fleet (" + satelliteCount + " satellites)");
        }

        // Check for global firsts
        Long globalFirsts = spaceMilestoneRepository.findByCountryIdOrderByDateAchievedAsc(countryId)
            .stream().filter(SpaceMilestone::getIsGlobalFirst).count();
        if (globalFirsts > 0) {
            strengths.add("Historic achievements (" + globalFirsts + " global firsts)");
        }

        analysis.put("strengths", strengths);

        // Weaknesses
        List<String> weaknesses = new ArrayList<>();

        if (!Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            weaknesses.add("No independent launch capability - dependent on partners");
        }
        if (!Boolean.TRUE.equals(country.getHumanSpaceflightCapable())) {
            weaknesses.add("No human spaceflight capability");
        }
        if (!Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            weaknesses.add("No reusable rocket technology - higher launch costs");
        }

        if (country.getOverallCapabilityScore() != null && country.getOverallCapabilityScore() < LOW_CAPABILITY_SCORE) {
            weaknesses.add("Low overall capability score (" + country.getOverallCapabilityScore() + ")");
        }

        if (country.getLaunchSuccessRate() != null && country.getLaunchSuccessRate() < CONCERNING_SUCCESS_RATE) {
            weaknesses.add("Launch reliability concerns (" + country.getLaunchSuccessRate() + "% success rate)");
        }

        Long launchSiteCount = launchSiteRepository.countByCountry(countryId);
        if (launchSiteCount == 0) {
            weaknesses.add("No domestic launch sites");
        }

        if (country.getAnnualBudgetUsd() != null &&
            country.getAnnualBudgetUsd().compareTo(BigDecimal.valueOf(LIMITED_BUDGET_USD)) < 0) {
            weaknesses.add("Limited budget (< $500M annually)");
        }

        analysis.put("weaknesses", weaknesses);

        // Opportunities
        List<String> opportunities = new ArrayList<>();

        if (!Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            opportunities.add("Develop reusable rocket technology to reduce launch costs");
        }
        if (!Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            opportunities.add("Expand into deep space exploration");
        }
        if (satelliteCount < 10) {
            opportunities.add("Expand satellite constellation for communications and Earth observation");
        }
        if (launchSiteCount == 0) {
            opportunities.add("Develop domestic launch infrastructure or partner with launch providers");
        }

        // Check for partnerships
        opportunities.add("International partnerships for technology sharing and cost reduction");
        opportunities.add("Commercial space sector development");

        analysis.put("opportunities", opportunities);

        // Threats
        List<String> threats = new ArrayList<>();

        threats.add("Growing competition from emerging space nations");
        threats.add("Space debris and orbital congestion risks");
        if (!Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            threats.add("Geopolitical risks affecting launch access");
        }
        if (country.getAnnualBudgetUsd() != null &&
            country.getAnnualBudgetUsd().compareTo(BigDecimal.valueOf(BUDGET_CONSTRAINT_THRESHOLD_USD)) < 0) {
            threats.add("Budget constraints limiting program expansion");
        }
        threats.add("Talent retention in competitive global space industry");

        analysis.put("threats", threats);

        // Calculate overall assessment
        Map<String, Object> assessment = new LinkedHashMap<>();
        assessment.put("strengthCount", strengths.size());
        assessment.put("weaknessCount", weaknesses.size());
        assessment.put("tier", determineTier(country));
        assessment.put("trajectory", determineTrajectory(country));

        analysis.put("overallAssessment", assessment);

        return analysis;
    }

    /**
     * Get rankings across all countries (BE-051)
     * Optimized: Uses database-level sorting via repository queries instead of in-memory sorting
     * Cached to avoid repeated expensive aggregation queries
     */
    @Cacheable(RANKINGS_CACHE)
    public Map<String, Object> getCountryRankings() {
        Map<String, Object> rankings = new LinkedHashMap<>();

        // Overall capability ranking - using database-level ORDER BY
        List<Country> byCapabilityScore = countryRepository.findAllOrderByCapabilityScoreDesc();
        List<Map<String, Object>> rankedByCapability = new ArrayList<>();
        for (int i = 0; i < byCapabilityScore.size(); i++) {
            Country c = byCapabilityScore.get(i);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", i + 1);
            entry.put("countryId", c.getId());
            entry.put("countryName", c.getName());
            entry.put("isoCode", c.getIsoCode());
            entry.put("score", c.getOverallCapabilityScore());
            rankedByCapability.add(entry);
        }
        rankings.put("byOverallCapability", rankedByCapability);

        // By total launches - using database-level ORDER BY
        List<Country> byLaunchCount = countryRepository.findTopByTotalLaunches();
        List<Map<String, Object>> rankedByLaunches = new ArrayList<>();
        for (int i = 0; i < byLaunchCount.size(); i++) {
            Country c = byLaunchCount.get(i);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", i + 1);
            entry.put("countryId", c.getId());
            entry.put("countryName", c.getName());
            entry.put("totalLaunches", c.getTotalLaunches());
            entry.put("successfulLaunches", c.getSuccessfulLaunches());
            entry.put("successRate", c.getLaunchSuccessRate());
            rankedByLaunches.add(entry);
        }
        rankings.put("byTotalLaunches", rankedByLaunches);

        // By budget - using database-level ORDER BY
        List<Country> byBudgetAmount = countryRepository.findCountriesWithBudgetOrderByBudgetDesc();
        List<Map<String, Object>> rankedByBudget = new ArrayList<>();
        for (int i = 0; i < byBudgetAmount.size(); i++) {
            Country c = byBudgetAmount.get(i);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", i + 1);
            entry.put("countryId", c.getId());
            entry.put("countryName", c.getName());
            entry.put("annualBudgetUsd", c.getAnnualBudgetUsd());
            entry.put("budgetAsPercentOfGdp", c.getBudgetAsPercentOfGdp());
            rankedByBudget.add(entry);
        }
        rankings.put("byAnnualBudget", rankedByBudget);

        // By success rate - using database-level ORDER BY
        List<Country> bySuccessRateList = countryRepository.findTopBySuccessRate();
        List<Map<String, Object>> rankedBySuccessRate = new ArrayList<>();
        for (int i = 0; i < bySuccessRateList.size(); i++) {
            Country c = bySuccessRateList.get(i);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", i + 1);
            entry.put("countryId", c.getId());
            entry.put("countryName", c.getName());
            entry.put("successRate", c.getLaunchSuccessRate());
            entry.put("totalLaunches", c.getTotalLaunches());
            rankedBySuccessRate.add(entry);
        }
        rankings.put("bySuccessRate", rankedBySuccessRate);

        rankings.put("totalCountries", countryRepository.count());

        return rankings;
    }

    // ==================== Private Helper Methods ====================

    private List<String> generateGapRecommendations(Country c1, Country c2) {
        List<String> recommendations = new ArrayList<>();

        // Compare and generate recommendations for the country with lower score
        Country leader = c1.getOverallCapabilityScore() != null && c2.getOverallCapabilityScore() != null &&
                        c1.getOverallCapabilityScore() > c2.getOverallCapabilityScore() ? c1 : c2;
        Country laggard = leader == c1 ? c2 : c1;

        if (Boolean.TRUE.equals(leader.getIndependentLaunchCapable()) &&
            !Boolean.TRUE.equals(laggard.getIndependentLaunchCapable())) {
            recommendations.add(laggard.getName() + " should develop independent launch capability to reduce dependency on partners");
        }

        if (Boolean.TRUE.equals(leader.getReusableRocketCapable()) &&
            !Boolean.TRUE.equals(laggard.getReusableRocketCapable())) {
            recommendations.add(laggard.getName() + " should invest in reusable rocket technology to reduce costs");
        }

        Long leaderEngines = engineRepository.countByCountryId(leader.getId());
        Long laggardEngines = engineRepository.countByCountryId(laggard.getId());
        if (leaderEngines > laggardEngines * 2) {
            recommendations.add(laggard.getName() + " should expand engine development program");
        }

        Long leaderSites = launchSiteRepository.countByCountry(leader.getId());
        Long laggardSites = launchSiteRepository.countByCountry(laggard.getId());
        if (leaderSites > laggardSites) {
            recommendations.add(laggard.getName() + " could benefit from additional launch infrastructure");
        }

        if (leader.getAnnualBudgetUsd() != null && laggard.getAnnualBudgetUsd() != null &&
            leader.getAnnualBudgetUsd().compareTo(laggard.getAnnualBudgetUsd().multiply(new BigDecimal("2"))) > 0) {
            recommendations.add(laggard.getName() + " may need increased space program funding to close the gap");
        }

        recommendations.add("Collaboration between " + c1.getName() + " and " + c2.getName() + " could be mutually beneficial");

        return recommendations;
    }

    private String determineTier(Country country) {
        Double score = country.getOverallCapabilityScore();
        if (score == null) return TIER_UNRANKED;
        if (score >= TIER_1_THRESHOLD) return TIER_1_NAME;
        if (score >= TIER_2_THRESHOLD) return TIER_2_NAME;
        if (score >= TIER_3_THRESHOLD) return TIER_3_NAME;
        if (score >= TIER_4_THRESHOLD) return TIER_4_NAME;
        return TIER_5_NAME;
    }

    private String determineTrajectory(Country country) {
        // This would ideally use historical data
        // For now, use heuristics based on recent activity indicators
        if (Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            return "Rising - Active in cutting-edge technology";
        }
        if (country.getOverallCapabilityScore() != null && country.getOverallCapabilityScore() > 50) {
            return "Stable - Maintaining strong position";
        }
        if (Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            return "Growing - Building launch capabilities";
        }
        return "Emerging - Developing space program";
    }

    private Map<String, Object> buildCountryBasicInfo(Country country) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", country.getId());
        info.put("name", country.getName());
        info.put("isoCode", country.getIsoCode());
        info.put("flagUrl", country.getFlagUrl());
        info.put("spaceAgency", country.getSpaceAgencyName());
        return info;
    }

    private Map<String, Object> buildCountryCapabilities(Country country) {
        Map<String, Object> caps = new LinkedHashMap<>();
        caps.put("countryId", country.getId());
        caps.put("overallScore", country.getOverallCapabilityScore());
        caps.put("hasLaunchCapability", country.getIndependentLaunchCapable());
        caps.put("hasHumanSpaceflight", country.getHumanSpaceflightCapable());
        caps.put("hasDeepSpace", country.getDeepSpaceCapable());
        caps.put("hasReusable", country.getReusableRocketCapable());
        return caps;
    }

    private Map<String, Object> buildCountryAssets(Country country) {
        Map<String, Object> assets = new LinkedHashMap<>();
        assets.put("countryId", country.getId());
        assets.put("engineCount", engineRepository.countByCountryId(country.getId()));
        assets.put("satelliteCount", satelliteRepository.countByCountry(country.getId()));
        assets.put("launchSiteCount", launchSiteRepository.countByCountry(country.getId()));
        assets.put("missionCount", spaceMissionRepository.countByCountry(country.getId()));
        return assets;
    }

    private Map<String, Object> calculateCountryRankings(List<Country> countries) {
        Map<String, Object> rankings = new LinkedHashMap<>();

        List<Map<String, Object>> byScore = countries.stream()
            .filter(c -> c.getOverallCapabilityScore() != null)
            .sorted((a, b) -> Double.compare(
                b.getOverallCapabilityScore(),
                a.getOverallCapabilityScore()))
            .map(c -> Map.of(
                "countryId", (Object) c.getId(),
                "countryName", c.getName(),
                "score", c.getOverallCapabilityScore()))
            .toList();
        rankings.put("byCapabilityScore", byScore);

        return rankings;
    }

    private Map<String, Object> buildDetailedCountryProfile(Country country) {
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.putAll(buildCountryBasicInfo(country));
        profile.put("capabilities", buildCountryCapabilities(country));
        profile.put("assets", buildCountryAssets(country));
        return profile;
    }

    private Map<String, Object> determineCountryWinners(Country c1, Country c2) {
        Map<String, Object> winners = new LinkedHashMap<>();

        Double score1 = c1.getOverallCapabilityScore();
        Double score2 = c2.getOverallCapabilityScore();

        if (score1 != null && score2 != null) {
            winners.put("overallScore", score1 > score2 ? c1.getName() : c2.getName());
        }

        // Asset counts
        Long engines1 = engineRepository.countByCountryId(c1.getId());
        Long engines2 = engineRepository.countByCountryId(c2.getId());
        winners.put("engineCount", engines1 > engines2 ? c1.getName() : c2.getName());

        Long sats1 = satelliteRepository.countByCountry(c1.getId());
        Long sats2 = satelliteRepository.countByCountry(c2.getId());
        winners.put("satelliteCount", sats1 > sats2 ? c1.getName() : c2.getName());

        return winners;
    }

    private Map<String, Object> buildEngineSpecs(Engine engine) {
        Map<String, Object> specs = new LinkedHashMap<>();
        specs.put("id", engine.getId());
        specs.put("name", engine.getName());
        specs.put("designer", engine.getDesigner());
        specs.put("country", engine.getCountry() != null ? engine.getCountry().getName() : engine.getOrigin());
        specs.put("thrustN", engine.getThrustN());
        specs.put("ispS", engine.getIsp_s());
        specs.put("propellant", engine.getPropellant());
        specs.put("powerCycle", engine.getPowerCycle());
        specs.put("status", engine.getStatus());
        return specs;
    }

    private Map<String, Object> buildEnginePerformanceComparison(List<Engine> engines) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        // Find max values
        double maxThrust = engines.stream()
            .filter(e -> e.getThrustN() != null)
            .mapToDouble(Engine::getThrustN)
            .max().orElse(0);

        double maxIsp = engines.stream()
            .filter(e -> e.getIsp_s() != null)
            .mapToDouble(Engine::getIsp_s)
            .max().orElse(0);

        comparison.put("maxThrustN", maxThrust);
        comparison.put("maxIspS", maxIsp);

        // Normalized comparison
        List<Map<String, Object>> normalized = engines.stream()
            .map(e -> {
                Map<String, Object> norm = new LinkedHashMap<>();
                norm.put("engineId", e.getId());
                norm.put("engineName", e.getName());
                norm.put("thrustNormalized", e.getThrustN() != null && maxThrust > 0 ?
                    Math.round(e.getThrustN() / maxThrust * 100) : 0);
                norm.put("ispNormalized", e.getIsp_s() != null && maxIsp > 0 ?
                    Math.round(e.getIsp_s() / maxIsp * 100) : 0);
                return norm;
            })
            .toList();

        comparison.put("normalizedScores", normalized);

        return comparison;
    }

    private Map<String, Object> calculateEngineRankings(List<Engine> engines) {
        Map<String, Object> rankings = new LinkedHashMap<>();

        List<Map<String, Object>> byThrust = engines.stream()
            .filter(e -> e.getThrustN() != null)
            .sorted((a, b) -> Long.compare(b.getThrustN(), a.getThrustN()))
            .map(e -> Map.of(
                "engineId", (Object) e.getId(),
                "engineName", e.getName(),
                "thrustN", e.getThrustN()))
            .toList();
        rankings.put("byThrust", byThrust);

        List<Map<String, Object>> byIsp = engines.stream()
            .filter(e -> e.getIsp_s() != null)
            .sorted((a, b) -> Double.compare(b.getIsp_s(), a.getIsp_s()))
            .map(e -> Map.of(
                "engineId", (Object) e.getId(),
                "engineName", e.getName(),
                "ispS", e.getIsp_s()))
            .toList();
        rankings.put("byIsp", byIsp);

        return rankings;
    }

    private double calculateEngineSimilarity(Engine ref, Engine other) {
        double score = 0;

        // Propellant type match
        if (ref.getPropellant() != null && ref.getPropellant().equals(other.getPropellant())) {
            score += 30;
        }

        // Cycle type match
        if (ref.getPowerCycle() != null && ref.getPowerCycle().equals(other.getPowerCycle())) {
            score += 30;
        }

        // Thrust similarity (within 30%)
        if (ref.getThrustN() != null && other.getThrustN() != null) {
            double ratio = (double) Math.min(ref.getThrustN(), other.getThrustN()) /
                Math.max(ref.getThrustN(), other.getThrustN());
            score += ratio * 20;
        }

        // ISP similarity
        if (ref.getIsp_s() != null && other.getIsp_s() != null) {
            double ratio = Math.min(ref.getIsp_s(), other.getIsp_s()) /
                Math.max(ref.getIsp_s(), other.getIsp_s());
            score += ratio * 20;
        }

        return score;
    }

    private Map<String, Object> buildSatelliteInfo(Satellite satellite) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", satellite.getId());
        info.put("name", satellite.getName());
        info.put("type", satellite.getSatelliteType());
        info.put("status", satellite.getStatus());
        info.put("country", satellite.getCountry() != null ? satellite.getCountry().getName() : null);
        info.put("operator", satellite.getOperator());
        info.put("orbitType", satellite.getOrbitType());
        info.put("altitudeKm", satellite.getAltitudeKm());
        info.put("massKg", satellite.getMassKg());
        info.put("launchDate", satellite.getLaunchDate());
        return info;
    }

    private Map<String, Object> buildOrbitalComparison(List<Satellite> satellites) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        Map<OrbitType, Long> byOrbit = satellites.stream()
            .filter(s -> s.getOrbitType() != null)
            .collect(Collectors.groupingBy(Satellite::getOrbitType, Collectors.counting()));
        comparison.put("byOrbitType", byOrbit);

        // Altitude comparison
        List<Map<String, Object>> altitudes = satellites.stream()
            .filter(s -> s.getAltitudeKm() != null)
            .map(s -> Map.of(
                "satelliteId", (Object) s.getId(),
                "satelliteName", s.getName(),
                "altitudeKm", s.getAltitudeKm()))
            .sorted((a, b) -> Double.compare(
                (Double) b.get("altitudeKm"),
                (Double) a.get("altitudeKm")))
            .toList();
        comparison.put("byAltitude", altitudes);

        return comparison;
    }

    private Map<String, Object> buildLaunchSiteInfo(LaunchSite site) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", site.getId());
        info.put("name", site.getName());
        info.put("shortName", site.getShortName());
        info.put("country", site.getCountry() != null ? site.getCountry().getName() : null);
        info.put("status", site.getStatus());
        info.put("latitude", site.getLatitude());
        info.put("longitude", site.getLongitude());
        info.put("operator", site.getOperator());
        info.put("establishedYear", site.getEstablishedYear());
        return info;
    }

    private Map<String, Object> buildLaunchSiteCapabilityComparison(List<LaunchSite> sites) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Map<String, Object>> capabilities = sites.stream()
            .map(site -> {
                Map<String, Object> caps = new LinkedHashMap<>();
                caps.put("siteId", site.getId());
                caps.put("siteName", site.getName());
                caps.put("humanRated", site.getHumanRatedCapable());
                caps.put("geoCapable", site.getSupportsGeo());
                caps.put("polarCapable", site.getSupportsPolar());
                caps.put("interplanetaryCapable", site.getSupportsInterplanetary());
                caps.put("hasLandingFacilities", site.getHasLandingFacilities());
                caps.put("numberOfPads", site.getNumberOfLaunchPads());

                // Calculate capability score
                int score = 0;
                if (Boolean.TRUE.equals(site.getHumanRatedCapable())) score += 25;
                if (Boolean.TRUE.equals(site.getSupportsGeo())) score += 20;
                if (Boolean.TRUE.equals(site.getSupportsPolar())) score += 15;
                if (Boolean.TRUE.equals(site.getSupportsInterplanetary())) score += 25;
                if (Boolean.TRUE.equals(site.getHasLandingFacilities())) score += 15;
                caps.put("capabilityScore", score);

                return caps;
            })
            .sorted((a, b) -> Integer.compare(
                (Integer) b.get("capabilityScore"),
                (Integer) a.get("capabilityScore")))
            .toList();

        comparison.put("capabilities", capabilities);

        return comparison;
    }

    private Map<String, Object> buildLocationComparison(List<LaunchSite> sites) {
        Map<String, Object> comparison = new LinkedHashMap<>();

        List<Map<String, Object>> locations = sites.stream()
            .filter(s -> s.getLatitude() != null && s.getLongitude() != null)
            .map(site -> {
                Map<String, Object> loc = new LinkedHashMap<>();
                loc.put("siteId", site.getId());
                loc.put("siteName", site.getName());
                loc.put("latitude", site.getLatitude());
                loc.put("longitude", site.getLongitude());
                loc.put("region", site.getRegion());

                // Calculate equatorial advantage (closer to equator = better for GEO)
                double equatorialAdvantage = 100 - Math.abs(site.getLatitude());
                loc.put("equatorialAdvantage", Math.round(equatorialAdvantage * 10.0) / 10.0);

                return loc;
            })
            .toList();

        comparison.put("locations", locations);

        return comparison;
    }
}
