package com.rocket.comparison.controller;

import com.rocket.comparison.entity.CapabilityCategory;
import com.rocket.comparison.entity.CapabilityScore;
import com.rocket.comparison.service.CapabilityScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class CapabilityScoreController {

    private final CapabilityScoreService scoreService;

    // ==================== Score Retrieval ====================

    /**
     * Get all scores for a country
     */
    @GetMapping("/country/{countryId}")
    public ResponseEntity<CountryScoreResponse> getScoresByCountry(@PathVariable Long countryId) {
        List<CapabilityScore> scores = scoreService.getScoresByCountry(countryId);
        if (scores.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CountryScoreResponse response = new CountryScoreResponse(scores);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific category score for a country
     */
    @GetMapping("/country/{countryId}/category/{category}")
    public ResponseEntity<CapabilityScore> getScoreByCategory(
            @PathVariable Long countryId,
            @PathVariable CapabilityCategory category) {
        return scoreService.getScoreByCountryAndCategory(countryId, category)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== Rankings ====================

    /**
     * Get global rankings (overall scores)
     */
    @GetMapping("/rankings")
    public ResponseEntity<List<RankingEntry>> getOverallRankings() {
        // Get rankings from any category and use overall score
        List<CapabilityScore> scores = scoreService.getRankingsByCategory(CapabilityCategory.LAUNCH_CAPABILITY);

        List<RankingEntry> rankings = scores.stream()
                .map(s -> new RankingEntry(
                        s.getRanking(),
                        s.getCountry().getId(),
                        s.getCountry().getName(),
                        s.getCountry().getIsoCode(),
                        s.getCountry().getSpaceAgencyAcronym(),
                        s.getCountry().getOverallCapabilityScore()
                ))
                .sorted(Comparator.comparing(RankingEntry::score, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        // Re-rank by overall score
        List<RankingEntry> reranked = new ArrayList<>();
        int rank = 1;
        for (RankingEntry entry : rankings) {
            reranked.add(new RankingEntry(rank++, entry.countryId(), entry.countryName(),
                    entry.isoCode(), entry.agencyAcronym(), entry.score()));
        }

        return ResponseEntity.ok(reranked);
    }

    /**
     * Get rankings by specific category
     */
    @GetMapping("/rankings/by-category/{category}")
    public ResponseEntity<CategoryRankingResponse> getRankingsByCategory(@PathVariable CapabilityCategory category) {
        List<CapabilityScore> scores = scoreService.getRankingsByCategory(category);
        Double average = scoreService.getAverageScoreByCategory(category);

        List<RankingEntry> rankings = scores.stream()
                .map(s -> new RankingEntry(
                        s.getRanking(),
                        s.getCountry().getId(),
                        s.getCountry().getName(),
                        s.getCountry().getIsoCode(),
                        s.getCountry().getSpaceAgencyAcronym(),
                        s.getScore()
                ))
                .toList();

        return ResponseEntity.ok(new CategoryRankingResponse(
                category.name(),
                category.getDisplayName(),
                category.getDescription(),
                category.getWeight(),
                average,
                rankings
        ));
    }

    // ==================== Score Calculation ====================

    /**
     * Trigger score recalculation for a country
     */
    @PostMapping("/recalculate/{countryId}")
    public ResponseEntity<CountryScoreResponse> recalculateScores(@PathVariable Long countryId) {
        try {
            List<CapabilityScore> scores = scoreService.calculateAndSaveScores(countryId);
            return ResponseEntity.ok(new CountryScoreResponse(scores));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Trigger score recalculation for all countries
     */
    @PostMapping("/recalculate-all")
    public ResponseEntity<Map<String, String>> recalculateAllScores() {
        scoreService.calculateAllScores();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All country scores have been recalculated"
        ));
    }

    // ==================== Comparison ====================

    /**
     * Compare scores between multiple countries
     */
    @GetMapping("/compare")
    public ResponseEntity<?> compareCountries(@RequestParam List<Long> countries) {
        if (countries == null || countries.size() < 2) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "At least 2 country IDs are required for comparison"
            ));
        }

        Map<String, Object> comparison = scoreService.compareCountries(countries);
        return ResponseEntity.ok(comparison);
    }

    // ==================== Category Info ====================

    /**
     * Get all capability categories with their weights
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryInfo>> getCategories() {
        List<CategoryInfo> categories = Arrays.stream(CapabilityCategory.values())
                .map(c -> new CategoryInfo(
                        c.name(),
                        c.getDisplayName(),
                        c.getDescription(),
                        c.getWeight()
                ))
                .toList();
        return ResponseEntity.ok(categories);
    }

    /**
     * Get detailed breakdown for a country's score
     */
    @GetMapping("/breakdown/{countryId}")
    public ResponseEntity<ScoreBreakdownResponse> getScoreBreakdown(@PathVariable Long countryId) {
        List<CapabilityScore> scores = scoreService.getScoresByCountry(countryId);
        if (scores.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Double overallScore = scores.stream()
                .mapToDouble(CapabilityScore::getWeightedScore)
                .sum();

        List<CategoryBreakdown> breakdowns = scores.stream()
                .map(s -> new CategoryBreakdown(
                        s.getCategory().name(),
                        s.getCategory().getDisplayName(),
                        s.getScore(),
                        s.getCategory().getWeight(),
                        s.getWeightedScore(),
                        s.getRanking(),
                        s.getScoreBreakdown()
                ))
                .toList();

        return ResponseEntity.ok(new ScoreBreakdownResponse(
                countryId,
                overallScore,
                breakdowns
        ));
    }

    // ==================== DTOs ====================

    public record CountryScoreResponse(
            Long countryId,
            String countryName,
            String isoCode,
            Double overallScore,
            List<CategoryScore> categoryScores
    ) {
        public CountryScoreResponse(List<CapabilityScore> scores) {
            this(
                    scores.isEmpty() ? null : scores.get(0).getCountry().getId(),
                    scores.isEmpty() ? null : scores.get(0).getCountry().getName(),
                    scores.isEmpty() ? null : scores.get(0).getCountry().getIsoCode(),
                    scores.isEmpty() ? null : scores.get(0).getCountry().getOverallCapabilityScore(),
                    scores.stream()
                            .map(s -> new CategoryScore(
                                    s.getCategory().name(),
                                    s.getCategory().getDisplayName(),
                                    s.getScore(),
                                    s.getRanking(),
                                    s.getCategory().getWeight()
                            ))
                            .toList()
            );
        }
    }

    public record CategoryScore(
            String category,
            String displayName,
            Double score,
            Integer ranking,
            Double weight
    ) {}

    public record RankingEntry(
            Integer rank,
            Long countryId,
            String countryName,
            String isoCode,
            String agencyAcronym,
            Double score
    ) {}

    public record CategoryRankingResponse(
            String category,
            String displayName,
            String description,
            Double weight,
            Double averageScore,
            List<RankingEntry> rankings
    ) {}

    public record CategoryInfo(
            String name,
            String displayName,
            String description,
            Double weight
    ) {}

    public record ScoreBreakdownResponse(
            Long countryId,
            Double overallScore,
            List<CategoryBreakdown> categories
    ) {}

    public record CategoryBreakdown(
            String category,
            String displayName,
            Double score,
            Double weight,
            Double weightedContribution,
            Integer globalRank,
            String detailedBreakdown
    ) {}
}
