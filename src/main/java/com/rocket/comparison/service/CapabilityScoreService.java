package com.rocket.comparison.service;

import com.rocket.comparison.entity.CapabilityCategory;
import com.rocket.comparison.entity.CapabilityScore;
import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.CapabilityScoreRepository;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for calculating Space Capability Index (SCI) scores.
 * Implements weighted scoring algorithms for each capability category.
 */
@Service
@RequiredArgsConstructor
public class CapabilityScoreService {

    private final CapabilityScoreRepository scoreRepository;
    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;

    private static final String CALCULATION_VERSION = "1.0";

    // ==================== Score Retrieval ====================

    public List<CapabilityScore> getScoresByCountry(Long countryId) {
        return scoreRepository.findByCountryId(countryId);
    }

    public Optional<CapabilityScore> getScoreByCountryAndCategory(Long countryId, CapabilityCategory category) {
        return scoreRepository.findByCountryIdAndCategory(countryId, category);
    }

    public List<CapabilityScore> getRankingsByCategory(CapabilityCategory category) {
        return scoreRepository.findByCategoryOrderByScoreDesc(category);
    }

    public Double getAverageScoreByCategory(CapabilityCategory category) {
        return scoreRepository.getAverageScoreByCategory(category);
    }

    // ==================== Overall Score Calculation ====================

    /**
     * Calculate the overall Space Capability Index for a country.
     * Uses weighted average of all category scores.
     */
    public Double calculateOverallScore(Country country) {
        double launchScore = calculateLaunchCapability(country);
        double propulsionScore = calculatePropulsionTechnology(country);
        double humanScore = calculateHumanSpaceflight(country);
        double deepSpaceScore = calculateDeepSpaceExploration(country);
        double satelliteScore = calculateSatelliteInfrastructure(country);
        double infrastructureScore = calculateSpaceInfrastructure(country);
        double independenceScore = calculateTechnologicalIndependence(country);

        return (launchScore * CapabilityCategory.LAUNCH_CAPABILITY.getWeight()) +
               (propulsionScore * CapabilityCategory.PROPULSION_TECHNOLOGY.getWeight()) +
               (humanScore * CapabilityCategory.HUMAN_SPACEFLIGHT.getWeight()) +
               (deepSpaceScore * CapabilityCategory.DEEP_SPACE_EXPLORATION.getWeight()) +
               (satelliteScore * CapabilityCategory.SATELLITE_INFRASTRUCTURE.getWeight()) +
               (infrastructureScore * CapabilityCategory.SPACE_INFRASTRUCTURE.getWeight()) +
               (independenceScore * CapabilityCategory.TECHNOLOGICAL_INDEPENDENCE.getWeight());
    }

    // ==================== Category Score Calculations ====================

    /**
     * LAUNCH_CAPABILITY (0-100)
     * - Active launch vehicles: 0-25 points (5 per vehicle, max 5)
     * - Payload capacity (max to LEO): 0-25 points
     * - Launch frequency (per year): 0-25 points
     * - Success rate: 0-25 points
     */
    public Double calculateLaunchCapability(Country country) {
        double score = 0;

        // Independent launch capability is prerequisite
        if (!Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            return 0.0;
        }

        // Base score for having launch capability
        score += 15;

        // Total launches contribute to experience
        if (country.getTotalLaunches() != null) {
            // Log scale for launches (max 25 points at 1000+ launches)
            score += Math.min(25, Math.log10(country.getTotalLaunches() + 1) * 8);
        }

        // Success rate (max 25 points for 95%+)
        if (country.getLaunchSuccessRate() != null) {
            score += Math.min(25, country.getLaunchSuccessRate() * 0.26);
        }

        // Reusability bonus
        if (Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            score += 10;
        }

        return Math.min(100, score);
    }

    /**
     * PROPULSION_TECHNOLOGY (0-100)
     * Based on engine portfolio analysis
     * - Engine count: 0-15 points
     * - Propellant diversity: 0-20 points
     * - Max specific impulse: 0-20 points
     * - Max chamber pressure: 0-15 points
     * - Advanced cycles: 0-15 points
     * - Reusable engine capability: 0-15 points
     */
    public Double calculatePropulsionTechnology(Country country) {
        List<Engine> engines = getEnginesForCountry(country);

        if (engines.isEmpty()) {
            return 0.0;
        }

        double score = 0;

        // Engine count (1.5 points per engine, max 15)
        score += Math.min(15, engines.size() * 1.5);

        // Propellant diversity
        Set<String> propellants = engines.stream()
            .map(Engine::getPropellant)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        score += Math.min(20, propellants.size() * 5);

        // Max ISP (normalize to 0-20, where 450s = 20 points)
        OptionalDouble maxIsp = engines.stream()
            .filter(e -> e.getIsp_s() != null)
            .mapToDouble(Engine::getIsp_s)
            .max();
        if (maxIsp.isPresent()) {
            score += Math.min(20, (maxIsp.getAsDouble() / 450.0) * 20);
        }

        // Max chamber pressure (normalize to 0-15, where 300 bar = 15 points)
        OptionalDouble maxPressure = engines.stream()
            .filter(e -> e.getChamberPressureBar() != null)
            .mapToDouble(Engine::getChamberPressureBar)
            .max();
        if (maxPressure.isPresent()) {
            score += Math.min(15, (maxPressure.getAsDouble() / 300.0) * 15);
        }

        // Advanced power cycles (staged combustion, full-flow)
        long advancedCycles = engines.stream()
            .filter(e -> e.getPowerCycle() != null)
            .filter(e -> e.getPowerCycle().toLowerCase().contains("staged") ||
                        e.getPowerCycle().toLowerCase().contains("full-flow"))
            .count();
        score += Math.min(15, advancedCycles * 5);

        // Reusability bonus
        if (Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            score += 10;
        }

        return Math.min(100, score);
    }

    /**
     * HUMAN_SPACEFLIGHT (0-100)
     * - Has capability: 30 base points
     * - Total crewed missions: 0-20 points
     * - Active astronaut corps: 0-15 points
     * - Space station capability: 20 points
     * - Long-duration capability: 0-15 points
     */
    public Double calculateHumanSpaceflight(Country country) {
        if (!Boolean.TRUE.equals(country.getHumanSpaceflightCapable())) {
            return 0.0;
        }

        double score = 30; // Base score for capability

        // Active astronauts (0.5 points per astronaut, max 15)
        if (country.getActiveAstronauts() != null) {
            score += Math.min(15, country.getActiveAstronauts() * 0.5);
        }

        // Space station capability
        if (Boolean.TRUE.equals(country.getSpaceStationCapable())) {
            score += 25;
        }

        // Launch experience bonus (proxy for crewed missions)
        if (country.getTotalLaunches() != null && country.getTotalLaunches() > 100) {
            score += 15;
        } else if (country.getTotalLaunches() != null && country.getTotalLaunches() > 50) {
            score += 10;
        }

        // Deep space human capability bonus
        if (Boolean.TRUE.equals(country.getLunarLandingCapable())) {
            score += 15;
        }

        return Math.min(100, score);
    }

    /**
     * DEEP_SPACE_EXPLORATION (0-100)
     * - Lunar missions: 0-25 points
     * - Mars missions: 0-25 points
     * - Other planetary: 0-20 points
     * - Sample return: 15 points
     * - Deep space probes: 0-15 points
     */
    public Double calculateDeepSpaceExploration(Country country) {
        if (!Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            return 0.0;
        }

        double score = 0;

        // Lunar capability
        if (Boolean.TRUE.equals(country.getLunarLandingCapable())) {
            score += 25;
        } else if (Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            score += 15; // Lunar orbit/flyby capability
        }

        // Mars capability
        if (Boolean.TRUE.equals(country.getMarsLandingCapable())) {
            score += 30;
        }

        // General deep space capability bonus
        if (Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            score += 20;
        }

        // Advanced capabilities inferred from overall program
        if (country.getTotalLaunches() != null && country.getTotalLaunches() > 500) {
            score += 10; // Extensive program likely includes deep space
        }

        return Math.min(100, score);
    }

    /**
     * SATELLITE_INFRASTRUCTURE (0-100)
     * - Active satellite count: 0-25 points (log scale)
     * - Satellite diversity: 0-20 points
     * - Indigenous GNSS: 20 points
     * - Mega-constellation: 20 points
     * - Space station: 15 points
     */
    public Double calculateSatelliteInfrastructure(Country country) {
        double score = 0;

        // Independent launch implies satellite capability
        if (Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            score += 20;
        }

        // Space station is a major satellite asset
        if (Boolean.TRUE.equals(country.getSpaceStationCapable())) {
            score += 25;
        }

        // Total launches as proxy for satellite deployments
        if (country.getTotalLaunches() != null) {
            score += Math.min(25, Math.log10(country.getTotalLaunches() + 1) * 8);
        }

        // Advanced programs have more diverse satellite types
        if (Boolean.TRUE.equals(country.getDeepSpaceCapable())) {
            score += 15; // Scientific satellites
        }

        // Reusability enables mega-constellations
        if (Boolean.TRUE.equals(country.getReusableRocketCapable())) {
            score += 15;
        }

        return Math.min(100, score);
    }

    /**
     * SPACE_INFRASTRUCTURE (0-100)
     * Based on ground infrastructure implied by capabilities
     */
    public Double calculateSpaceInfrastructure(Country country) {
        double score = 0;

        // Launch sites (implied by launch capability)
        if (Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            score += 30;
        }

        // Employees as infrastructure indicator
        if (country.getTotalSpaceAgencyEmployees() != null) {
            score += Math.min(25, Math.log10(country.getTotalSpaceAgencyEmployees() + 1) * 6);
        }

        // Budget as infrastructure indicator
        if (country.getAnnualBudgetUsd() != null) {
            double budgetBillions = country.getAnnualBudgetUsd().doubleValue() / 1_000_000_000;
            score += Math.min(25, budgetBillions * 2);
        }

        // Human spaceflight requires extensive ground infrastructure
        if (Boolean.TRUE.equals(country.getHumanSpaceflightCapable())) {
            score += 20;
        }

        return Math.min(100, score);
    }

    /**
     * TECHNOLOGICAL_INDEPENDENCE (0-100)
     * - Indigenous launch: 25 points
     * - Indigenous engines: 25 points
     * - Indigenous satellites: 25 points
     * - End-to-end capability: 25 points
     */
    public Double calculateTechnologicalIndependence(Country country) {
        double score = 0;

        // Indigenous launch capability
        if (Boolean.TRUE.equals(country.getIndependentLaunchCapable())) {
            score += 30;
        }

        // Has own engines
        List<Engine> engines = getEnginesForCountry(country);
        if (!engines.isEmpty()) {
            score += 25;
        }

        // Human spaceflight independence
        if (Boolean.TRUE.equals(country.getHumanSpaceflightCapable())) {
            score += 25;
        }

        // Full space station capability shows end-to-end independence
        if (Boolean.TRUE.equals(country.getSpaceStationCapable())) {
            score += 20;
        }

        return Math.min(100, score);
    }

    // ==================== Full Calculation & Persistence ====================

    /**
     * Calculate and save all scores for a country
     */
    @Transactional
    public List<CapabilityScore> calculateAndSaveScores(Long countryId) {
        Country country = countryRepository.findById(countryId)
            .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryId));

        // Delete existing scores
        scoreRepository.deleteByCountryId(countryId);

        List<CapabilityScore> scores = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Calculate each category
        for (CapabilityCategory category : CapabilityCategory.values()) {
            Double categoryScore = calculateCategoryScore(country, category);

            CapabilityScore score = new CapabilityScore();
            score.setCountry(country);
            score.setCategory(category);
            score.setScore(categoryScore);
            score.setCalculatedAt(now);
            score.setCalculationVersion(CALCULATION_VERSION);
            score.setScoreBreakdown(generateBreakdown(country, category));

            scores.add(scoreRepository.save(score));
        }

        // Update overall score on country
        Double overallScore = calculateOverallScore(country);
        country.setOverallCapabilityScore(overallScore);
        countryRepository.save(country);

        // Update rankings
        updateRankings();

        return scores;
    }

    /**
     * Calculate scores for all countries
     */
    @Transactional
    public void calculateAllScores() {
        List<Country> countries = countryRepository.findAll();
        for (Country country : countries) {
            calculateAndSaveScores(country.getId());
        }
    }

    /**
     * Update rankings for all categories
     */
    @Transactional
    public void updateRankings() {
        for (CapabilityCategory category : CapabilityCategory.values()) {
            List<CapabilityScore> scores = scoreRepository.findByCategoryOrderByScoreDesc(category);
            int rank = 1;
            for (CapabilityScore score : scores) {
                score.setRanking(rank++);
                scoreRepository.save(score);
            }
        }
    }

    // ==================== Helper Methods ====================

    private Double calculateCategoryScore(Country country, CapabilityCategory category) {
        return switch (category) {
            case LAUNCH_CAPABILITY -> calculateLaunchCapability(country);
            case PROPULSION_TECHNOLOGY -> calculatePropulsionTechnology(country);
            case HUMAN_SPACEFLIGHT -> calculateHumanSpaceflight(country);
            case DEEP_SPACE_EXPLORATION -> calculateDeepSpaceExploration(country);
            case SATELLITE_INFRASTRUCTURE -> calculateSatelliteInfrastructure(country);
            case SPACE_INFRASTRUCTURE -> calculateSpaceInfrastructure(country);
            case TECHNOLOGICAL_INDEPENDENCE -> calculateTechnologicalIndependence(country);
        };
    }

    private List<Engine> getEnginesForCountry(Country country) {
        // Try to get by country relationship first
        List<Engine> engines = engineRepository.findByCountryId(country.getId());

        // Fallback to origin string match
        if (engines.isEmpty() && country.getName() != null) {
            engines = engineRepository.findByOrigin(country.getName());
        }
        if (engines.isEmpty() && country.getIsoCode() != null) {
            engines = engineRepository.findByOrigin(country.getIsoCode());
        }

        return engines;
    }

    private String generateBreakdown(Country country, CapabilityCategory category) {
        // Generate a simple JSON breakdown for transparency
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("category", category.name());
        breakdown.put("score", calculateCategoryScore(country, category));
        breakdown.put("weight", category.getWeight());
        breakdown.put("weightedContribution", calculateCategoryScore(country, category) * category.getWeight());

        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(breakdown);
        } catch (Exception e) {
            return "{}";
        }
    }

    // ==================== Comparison Methods ====================

    /**
     * Get comparative analysis between countries
     */
    public Map<String, Object> compareCountries(List<Long> countryIds) {
        Map<String, Object> comparison = new HashMap<>();

        List<Map<String, Object>> countryScores = new ArrayList<>();
        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId).orElse(null);
            if (country != null) {
                Map<String, Object> countryData = new HashMap<>();
                countryData.put("id", country.getId());
                countryData.put("name", country.getName());
                countryData.put("isoCode", country.getIsoCode());
                countryData.put("overallScore", country.getOverallCapabilityScore());

                List<CapabilityScore> scores = scoreRepository.findByCountryId(countryId);
                Map<String, Double> categoryScores = scores.stream()
                    .collect(Collectors.toMap(
                        s -> s.getCategory().name(),
                        CapabilityScore::getScore
                    ));
                countryData.put("categoryScores", categoryScores);

                countryScores.add(countryData);
            }
        }

        comparison.put("countries", countryScores);
        comparison.put("categories", Arrays.stream(CapabilityCategory.values())
            .map(c -> Map.of("name", c.name(), "displayName", c.getDisplayName(), "weight", c.getWeight()))
            .collect(Collectors.toList()));

        return comparison;
    }
}
