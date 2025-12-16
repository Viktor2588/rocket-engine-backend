package com.rocket.comparison.controller;

import com.rocket.comparison.entity.MilestoneType;
import com.rocket.comparison.entity.SpaceMilestone;
import com.rocket.comparison.service.SpaceMilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
public class SpaceMilestoneController {

    private final SpaceMilestoneService milestoneService;

    // ==================== Basic CRUD ====================

    @GetMapping
    public ResponseEntity<List<SpaceMilestone>> getAllMilestones() {
        return ResponseEntity.ok(milestoneService.getAllMilestones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMilestone> getMilestoneById(@PathVariable Long id) {
        return milestoneService.getMilestoneById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SpaceMilestone> createMilestone(@Valid @RequestBody SpaceMilestone milestone) {
        SpaceMilestone saved = milestoneService.saveMilestone(milestone);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceMilestone> updateMilestone(
            @PathVariable Long id,
            @Valid @RequestBody SpaceMilestone milestone) {
        try {
            SpaceMilestone updated = milestoneService.updateMilestone(id, milestone);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
        if (milestoneService.getMilestoneById(id).isPresent()) {
            milestoneService.deleteMilestone(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== By Country ====================

    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(milestoneService.getMilestonesByCountry(countryId));
    }

    @GetMapping("/by-country-code/{isoCode}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByCountryCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(milestoneService.getMilestonesByCountryCode(isoCode));
    }

    @GetMapping("/by-country/{countryId}/firsts")
    public ResponseEntity<List<SpaceMilestone>> getFirstsByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(milestoneService.getFirstsByCountry(countryId));
    }

    // ==================== By Milestone Type ====================

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByType(@PathVariable MilestoneType type) {
        return ResponseEntity.ok(milestoneService.getMilestonesByType(type));
    }

    @GetMapping("/by-type/{type}/first")
    public ResponseEntity<SpaceMilestone> getFirstAchiever(@PathVariable MilestoneType type) {
        return milestoneService.getFirstAchiever(type)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(milestoneService.getMilestonesByCategory(category));
    }

    // ==================== Timeline ====================

    @GetMapping("/timeline")
    public ResponseEntity<List<SpaceMilestone>> getTimeline(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        if (startYear == null) startYear = 1957;
        if (endYear == null) endYear = java.time.LocalDate.now().getYear();
        return ResponseEntity.ok(milestoneService.getTimeline(startYear, endYear));
    }

    @GetMapping("/by-year/{year}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(milestoneService.getMilestonesByYear(year));
    }

    @GetMapping("/by-decade/{decade}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByDecade(@PathVariable Integer decade) {
        return ResponseEntity.ok(milestoneService.getMilestonesByDecade(decade));
    }

    @GetMapping("/by-era/{era}")
    public ResponseEntity<List<SpaceMilestone>> getMilestonesByEra(@PathVariable String era) {
        return ResponseEntity.ok(milestoneService.getMilestonesByEra(era));
    }

    // ==================== Global Firsts ====================

    @GetMapping("/first-achievers")
    public ResponseEntity<List<SpaceMilestone>> getAllGlobalFirsts() {
        return ResponseEntity.ok(milestoneService.getAllGlobalFirsts());
    }

    @GetMapping("/firsts-leaderboard")
    public ResponseEntity<Map<String, Long>> getFirstsLeaderboard() {
        return ResponseEntity.ok(milestoneService.getFirstsLeaderboard());
    }

    // ==================== Search ====================

    @GetMapping("/search")
    public ResponseEntity<List<SpaceMilestone>> searchMilestones(@RequestParam String q) {
        return ResponseEntity.ok(milestoneService.searchMilestones(q));
    }

    // ==================== Comparison ====================

    @GetMapping("/compare-timelines")
    public ResponseEntity<Map<String, Object>> compareTimelines(
            @RequestParam List<Long> countries,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(milestoneService.compareTimelines(countries, startYear, endYear));
    }

    @GetMapping("/space-race")
    public ResponseEntity<Map<String, Object>> getSpaceRaceTimeline(
            @RequestParam Long country1,
            @RequestParam Long country2) {
        return ResponseEntity.ok(milestoneService.getSpaceRaceTimeline(country1, country2));
    }

    // ==================== Meta Information ====================

    @GetMapping("/types")
    public ResponseEntity<List<MilestoneTypeInfo>> getMilestoneTypes() {
        List<MilestoneTypeInfo> types = Arrays.stream(MilestoneType.values())
                .map(t -> new MilestoneTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/types/by-category/{category}")
    public ResponseEntity<List<MilestoneTypeInfo>> getMilestoneTypesByCategory(@PathVariable String category) {
        List<MilestoneTypeInfo> types = MilestoneType.getByCategory(category).stream()
                .map(t -> new MilestoneTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(MilestoneType.getAllCategories());
    }

    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAllYears() {
        return ResponseEntity.ok(milestoneService.getAllYears());
    }

    @GetMapping("/decades")
    public ResponseEntity<List<Integer>> getAllDecades() {
        return ResponseEntity.ok(milestoneService.getAllDecades());
    }

    @GetMapping("/eras")
    public ResponseEntity<List<String>> getAllEras() {
        return ResponseEntity.ok(milestoneService.getAllEras());
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    public ResponseEntity<MilestoneStatistics> getStatistics() {
        // BE-010: Use COUNT queries instead of loading all entities
        return ResponseEntity.ok(new MilestoneStatistics(
                milestoneService.countAll(),
                milestoneService.countGlobalFirsts(),
                milestoneService.countYearsSpanned(),
                milestoneService.countByCategory(),
                milestoneService.countByEra(),
                milestoneService.getFirstsLeaderboard()
        ));
    }

    // ==================== DTOs ====================

    public record MilestoneTypeInfo(
            String name,
            String displayName,
            String category,
            String description
    ) {}

    public record MilestoneStatistics(
            Long totalMilestones,
            Long globalFirsts,
            Integer yearsSpanned,
            Map<String, Long> byCategory,
            Map<String, Long> byEra,
            Map<String, Long> firstsLeaderboard
    ) {}
}
