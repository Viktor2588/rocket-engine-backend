package com.rocket.comparison.controller;

import com.rocket.comparison.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller providing analytics and trend analysis endpoints.
 * Supports historical data analysis, trend detection, and records tracking.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // ==================== Launch Analytics ====================

    /**
     * Get global launches per year
     * Returns: Mission counts by year with totals and averages
     */
    @GetMapping("/launches-per-year")
    public ResponseEntity<Map<String, Object>> getLaunchesPerYear() {
        return ResponseEntity.ok(analyticsService.getLaunchesPerYear());
    }

    /**
     * Get launches per year broken down by country
     * Returns: Per-country mission counts with yearly breakdown
     */
    @GetMapping("/launches-per-year/by-country")
    public ResponseEntity<Map<String, Object>> getLaunchesPerYearByCountry() {
        return ResponseEntity.ok(analyticsService.getLaunchesPerYearByCountry());
    }

    // ==================== Capability Analysis ====================

    /**
     * Get capability growth analysis for a country
     * Returns: Current scores, category breakdown, and milestone timeline
     */
    @GetMapping("/capability-growth/{countryId}")
    public ResponseEntity<Map<String, Object>> getCapabilityGrowth(@PathVariable Long countryId) {
        return ResponseEntity.ok(analyticsService.getCapabilityGrowth(countryId));
    }

    // ==================== Emerging Nations ====================

    /**
     * Identify emerging space powers
     * Returns: List of rising space nations based on recent activity
     */
    @GetMapping("/emerging-nations")
    public ResponseEntity<Map<String, Object>> getEmergingNations() {
        return ResponseEntity.ok(analyticsService.getEmergingNations());
    }

    // ==================== Technology Trends ====================

    /**
     * Get technology trends analysis
     * Returns: Propellant, cycle, reusability, and satellite type trends
     */
    @GetMapping("/technology-trends")
    public ResponseEntity<Map<String, Object>> getTechnologyTrends() {
        return ResponseEntity.ok(analyticsService.getTechnologyTrends());
    }

    // ==================== Records ====================

    /**
     * Get current world records
     * Returns: Records for engines, countries, missions, and satellites
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getRecords() {
        return ResponseEntity.ok(analyticsService.getRecords());
    }

    // ==================== Summary ====================

    /**
     * Get comprehensive analytics summary
     * Returns: Combined launch trends, technology trends, and records
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary() {
        return ResponseEntity.ok(analyticsService.getAnalyticsSummary());
    }
}
