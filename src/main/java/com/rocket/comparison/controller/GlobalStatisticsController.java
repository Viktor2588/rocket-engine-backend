package com.rocket.comparison.controller;

import com.rocket.comparison.service.GlobalStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller providing comprehensive global statistics across all space entities.
 * Endpoints aggregate data from countries, engines, satellites, missions, and infrastructure.
 */
@RestController
@RequiredArgsConstructor
public class GlobalStatisticsController {

    // Support both /api/statistics and /api/global-statistics for frontend compatibility

    private final GlobalStatisticsService globalStatisticsService;

    // ==================== Overview ====================

    /**
     * Get comprehensive global overview
     * Returns: Key metrics across all entity types
     */
    @GetMapping({"/api/statistics/overview", "/api/global-statistics", "/api/statistics"})
    public ResponseEntity<Map<String, Object>> getGlobalOverview() {
        return ResponseEntity.ok(globalStatisticsService.getGlobalOverview());
    }

    /**
     * Get all entity counts broken down by type
     * Returns: Counts for countries, engines, satellites, launch sites, missions, milestones
     */
    @GetMapping({"/api/statistics/counts", "/api/global-statistics/counts"})
    public ResponseEntity<Map<String, Object>> getEntityCounts() {
        return ResponseEntity.ok(globalStatisticsService.getEntityCounts());
    }

    // ==================== Country Statistics ====================

    /**
     * Get statistics for all countries
     * Returns: Per-country counts and capability scores
     */
    @GetMapping({"/api/statistics/countries", "/api/global-statistics/countries"})
    public ResponseEntity<List<Map<String, Object>>> getCountryStatistics() {
        return ResponseEntity.ok(globalStatisticsService.getCountryStatistics());
    }

    /**
     * Get top countries by various metrics
     * Returns: Rankings by capability, missions, satellites
     */
    @GetMapping({"/api/statistics/countries/top", "/api/global-statistics/countries/top"})
    public ResponseEntity<Map<String, Object>> getTopCountries(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(globalStatisticsService.getTopCountries(limit));
    }

    // ==================== Technology Statistics ====================

    /**
     * Get engine technology breakdown
     * Returns: Stats by propellant type, cycle type, performance metrics
     */
    @GetMapping({"/api/statistics/technology/engines", "/api/global-statistics/technology/engines"})
    public ResponseEntity<Map<String, Object>> getEngineTechnologyStats() {
        return ResponseEntity.ok(globalStatisticsService.getEngineTechnologyStats());
    }

    /**
     * Get satellite technology breakdown
     * Returns: Stats by satellite type, orbit type, constellation data
     */
    @GetMapping({"/api/statistics/technology/satellites", "/api/global-statistics/technology/satellites"})
    public ResponseEntity<Map<String, Object>> getSatelliteTechnologyStats() {
        return ResponseEntity.ok(globalStatisticsService.getSatelliteTechnologyStats());
    }

    // ==================== Infrastructure Statistics ====================

    /**
     * Get launch infrastructure statistics
     * Returns: Launch site capabilities and geographic distribution
     */
    @GetMapping({"/api/statistics/infrastructure", "/api/global-statistics/infrastructure"})
    public ResponseEntity<Map<String, Object>> getLaunchInfrastructureStats() {
        return ResponseEntity.ok(globalStatisticsService.getLaunchInfrastructureStats());
    }

    // ==================== Mission Statistics ====================

    /**
     * Get comprehensive mission statistics
     * Returns: Success rates, mission types, yearly breakdown
     */
    @GetMapping({"/api/statistics/missions", "/api/global-statistics/missions"})
    public ResponseEntity<Map<String, Object>> getMissionStats() {
        return ResponseEntity.ok(globalStatisticsService.getMissionStats());
    }

    /**
     * Get mission success rates by country
     * Returns: Per-country success rates ranked by performance
     */
    @GetMapping({"/api/statistics/missions/success-rates", "/api/global-statistics/missions/success-rates"})
    public ResponseEntity<List<Map<String, Object>>> getMissionSuccessRatesByCountry() {
        return ResponseEntity.ok(globalStatisticsService.getMissionSuccessRatesByCountry());
    }

    // ==================== Timeline Statistics ====================

    /**
     * Get historical statistics by decade
     * Returns: Milestones and missions grouped by decade
     */
    @GetMapping({"/api/statistics/timeline/by-decade", "/api/global-statistics/timeline/by-decade"})
    public ResponseEntity<Map<String, Object>> getStatsByDecade() {
        return ResponseEntity.ok(globalStatisticsService.getStatsByDecade());
    }

    /**
     * Get year-over-year growth statistics
     * Returns: Current vs previous year comparison with growth percentages
     */
    @GetMapping({"/api/statistics/timeline/growth", "/api/global-statistics/timeline/growth"})
    public ResponseEntity<Map<String, Object>> getYearOverYearGrowth() {
        return ResponseEntity.ok(globalStatisticsService.getYearOverYearGrowth());
    }
}
