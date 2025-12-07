package com.rocket.comparison.controller;

import com.rocket.comparison.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller providing aggregated data optimized for frontend visualizations.
 * Endpoints return data formatted for charts, maps, and comparison views.
 */
@RestController
@RequestMapping("/api/visualizations")
@RequiredArgsConstructor
public class VisualizationController {

    private final VisualizationService visualizationService;

    // ==================== Map Data ====================

    /**
     * Get country data for world map visualization
     * Returns: countries with capability scores and flags for coloring/tooltips
     */
    @GetMapping("/map/countries")
    public ResponseEntity<List<Map<String, Object>>> getWorldMapData() {
        return ResponseEntity.ok(visualizationService.getWorldMapData());
    }

    /**
     * Get launch site markers for map
     * Returns: launch sites with coordinates and status for map markers
     */
    @GetMapping("/map/launch-sites")
    public ResponseEntity<List<Map<String, Object>>> getLaunchSiteMapData() {
        return ResponseEntity.ok(visualizationService.getLaunchSiteMapData());
    }

    // ==================== Timeline Data ====================

    /**
     * Get milestone timeline data
     * Returns: milestones grouped by year and country for timeline visualization
     */
    @GetMapping("/timeline/milestones")
    public ResponseEntity<Map<String, Object>> getMilestoneTimeline(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(visualizationService.getMilestoneTimeline(startYear, endYear));
    }

    /**
     * Get mission timeline data
     * Returns: mission counts by year, country, and type for stacked charts
     */
    @GetMapping("/timeline/missions")
    public ResponseEntity<Map<String, Object>> getMissionTimeline(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(visualizationService.getMissionTimeline(startYear, endYear));
    }

    // ==================== Chart Data ====================

    /**
     * Get radar chart data for capability comparison
     * Returns: category scores for selected countries in radar chart format
     */
    @GetMapping({"/charts/capability-radar", "/radar-country-capabilities"})
    public ResponseEntity<Map<String, Object>> getCapabilityRadarData(
            @RequestParam(required = false) List<Long> countries) {
        if (countries == null || countries.isEmpty()) {
            return ResponseEntity.ok(visualizationService.getAllCountriesCapabilityRadarData());
        }
        return ResponseEntity.ok(visualizationService.getCapabilityRadarData(countries));
    }

    /**
     * Get engine comparison bubble chart data
     * Returns: engines with thrust vs ISP for bubble chart (size = thrust)
     */
    @GetMapping({"/charts/engine-bubble", "/bubble-engine-performance"})
    public ResponseEntity<List<Map<String, Object>>> getEngineBubbleChartData() {
        return ResponseEntity.ok(visualizationService.getEngineBubbleChartData());
    }

    /**
     * Get satellite distribution treemap data
     * Returns: satellite counts by type, country, and constellation
     */
    @GetMapping("/charts/satellite-treemap")
    public ResponseEntity<Map<String, Object>> getSatelliteTreemapData() {
        return ResponseEntity.ok(visualizationService.getSatelliteTreemapData());
    }

    /**
     * Get launch frequency stacked bar chart data
     * Returns: mission counts by country per year for stacked bar chart
     */
    @GetMapping("/charts/launch-frequency")
    public ResponseEntity<Map<String, Object>> getLaunchFrequencyChartData(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(visualizationService.getLaunchFrequencyChartData(startYear, endYear));
    }

    // ==================== Rankings ====================

    /**
     * Get country rankings for leaderboard
     * Returns: ranked list of countries by capability score
     */
    @GetMapping("/rankings/countries")
    public ResponseEntity<List<Map<String, Object>>> getCountryRankings() {
        return ResponseEntity.ok(visualizationService.getCountryRankings());
    }

    /**
     * Get historic firsts leaderboard
     * Returns: countries ranked by number of "first" achievements
     */
    @GetMapping("/rankings/firsts")
    public ResponseEntity<List<Map<String, Object>>> getFirstsLeaderboard() {
        return ResponseEntity.ok(visualizationService.getFirstsLeaderboard());
    }

    // ==================== Dashboard ====================

    /**
     * Get dashboard summary data
     * Returns: key metrics for dashboard cards/widgets
     */
    @GetMapping("/dashboard/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(visualizationService.getDashboardSummary());
    }
}
