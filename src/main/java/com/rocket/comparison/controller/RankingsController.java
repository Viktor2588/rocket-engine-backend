package com.rocket.comparison.controller;

import com.rocket.comparison.service.ComparisonService;
import com.rocket.comparison.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller providing global rankings and leaderboard endpoints.
 * Dedicated endpoint for quick access to rankings across all dimensions.
 */
@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingsController {

    private final ComparisonService comparisonService;
    private final AnalyticsService analyticsService;

    /**
     * Get comprehensive country rankings
     * Returns: Rankings by capability, launches, budget, success rate
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCountryRankings() {
        return ResponseEntity.ok(comparisonService.getCountryRankings());
    }

    /**
     * Get rankings by overall capability score
     */
    @GetMapping("/by-capability")
    public ResponseEntity<Map<String, Object>> getRankingsByCapability() {
        Map<String, Object> rankings = comparisonService.getCountryRankings();
        return ResponseEntity.ok(Map.of(
            "rankings", rankings.get("byOverallCapability"),
            "totalCountries", rankings.get("totalCountries")
        ));
    }

    /**
     * Get rankings by total launches
     */
    @GetMapping("/by-launches")
    public ResponseEntity<Map<String, Object>> getRankingsByLaunches() {
        Map<String, Object> rankings = comparisonService.getCountryRankings();
        return ResponseEntity.ok(Map.of(
            "rankings", rankings.get("byTotalLaunches"),
            "totalCountries", rankings.get("totalCountries")
        ));
    }

    /**
     * Get rankings by annual budget
     */
    @GetMapping("/by-budget")
    public ResponseEntity<Map<String, Object>> getRankingsByBudget() {
        Map<String, Object> rankings = comparisonService.getCountryRankings();
        return ResponseEntity.ok(Map.of(
            "rankings", rankings.get("byAnnualBudget"),
            "totalCountries", rankings.get("totalCountries")
        ));
    }

    /**
     * Get rankings by success rate (min 10 launches)
     */
    @GetMapping("/by-success-rate")
    public ResponseEntity<Map<String, Object>> getRankingsBySuccessRate() {
        Map<String, Object> rankings = comparisonService.getCountryRankings();
        return ResponseEntity.ok(Map.of(
            "rankings", rankings.get("bySuccessRate"),
            "totalCountries", rankings.get("totalCountries")
        ));
    }

    /**
     * Get emerging space nations rankings
     * Returns: Ranking of emerging nations based on recent activity
     */
    @GetMapping("/emerging")
    public ResponseEntity<Map<String, Object>> getEmergingNationsRanking() {
        return ResponseEntity.ok(analyticsService.getEmergingNations());
    }

    /**
     * Get world records across all categories
     * Returns: Current record holders for engines, countries, missions, satellites
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getWorldRecords() {
        return ResponseEntity.ok(analyticsService.getRecords());
    }
}
