package com.rocket.comparison.controller;

import com.rocket.comparison.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller providing advanced side-by-side comparison capabilities.
 * Endpoints enable detailed comparisons between countries, engines, satellites, and more.
 */
@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    // ==================== Country Comparisons ====================

    /**
     * Compare multiple countries across all dimensions
     * @param countryIds List of country IDs to compare
     * Returns: Comprehensive comparison including capabilities, assets, missions
     */
    @GetMapping("/countries")
    public ResponseEntity<Map<String, Object>> compareCountries(
            @RequestParam List<Long> countryIds) {
        return ResponseEntity.ok(comparisonService.compareCountries(countryIds));
    }

    /**
     * Head-to-head comparison between two countries
     * @param countryId1 First country ID
     * @param countryId2 Second country ID
     * Returns: Detailed comparison with winner determination per category
     */
    @GetMapping("/countries/head-to-head")
    public ResponseEntity<Map<String, Object>> headToHeadCountryComparison(
            @RequestParam Long countryId1,
            @RequestParam Long countryId2) {
        return ResponseEntity.ok(comparisonService.headToHeadCountryComparison(countryId1, countryId2));
    }

    /**
     * Compare technology levels across countries
     * @param countryIds List of country IDs to compare
     * Returns: Technology assessment for engines, satellites, and launch capability
     */
    @GetMapping("/countries/technology")
    public ResponseEntity<Map<String, Object>> compareTechnologyLevels(
            @RequestParam List<Long> countryIds) {
        return ResponseEntity.ok(comparisonService.compareTechnologyLevels(countryIds));
    }

    // ==================== Engine Comparisons ====================

    /**
     * Compare multiple engines
     * @param engineIds List of engine IDs to compare
     * Returns: Specs, performance metrics, technology comparison, rankings
     */
    @GetMapping("/engines")
    public ResponseEntity<Map<String, Object>> compareEngines(
            @RequestParam List<Long> engineIds) {
        return ResponseEntity.ok(comparisonService.compareEngines(engineIds));
    }

    /**
     * Find similar engines to a reference engine
     * @param engineId Reference engine ID
     * @param limit Max number of similar engines to return
     * Returns: List of similar engines with similarity scores
     */
    @GetMapping("/engines/{engineId}/similar")
    public ResponseEntity<List<Map<String, Object>>> findSimilarEngines(
            @PathVariable Long engineId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(comparisonService.findSimilarEngines(engineId, limit));
    }

    // ==================== Satellite Comparisons ====================

    /**
     * Compare multiple satellites
     * @param satelliteIds List of satellite IDs to compare
     * Returns: Basic info, orbital comparison, technical specs comparison
     */
    @GetMapping("/satellites")
    public ResponseEntity<Map<String, Object>> compareSatellites(
            @RequestParam List<Long> satelliteIds) {
        return ResponseEntity.ok(comparisonService.compareSatellites(satelliteIds));
    }

    /**
     * Compare satellite constellations
     * @param constellations List of constellation names to compare
     * Returns: Constellation statistics including satellite counts, coverage, operators
     */
    @GetMapping("/satellites/constellations")
    public ResponseEntity<Map<String, Object>> compareConstellations(
            @RequestParam List<String> constellations) {
        return ResponseEntity.ok(comparisonService.compareConstellations(constellations));
    }

    // ==================== Launch Site Comparisons ====================

    /**
     * Compare multiple launch sites
     * @param siteIds List of launch site IDs to compare
     * Returns: Basic info, capability comparison, location analysis, performance stats
     */
    @GetMapping("/launch-sites")
    public ResponseEntity<Map<String, Object>> compareLaunchSites(
            @RequestParam List<Long> siteIds) {
        return ResponseEntity.ok(comparisonService.compareLaunchSites(siteIds));
    }
}
