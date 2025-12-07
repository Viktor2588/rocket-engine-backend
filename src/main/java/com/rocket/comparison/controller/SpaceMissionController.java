package com.rocket.comparison.controller;

import com.rocket.comparison.entity.Destination;
import com.rocket.comparison.entity.MissionStatus;
import com.rocket.comparison.entity.MissionType;
import com.rocket.comparison.entity.SpaceMission;
import com.rocket.comparison.service.SpaceMissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class SpaceMissionController {

    private final SpaceMissionService missionService;

    // ==================== Basic CRUD ====================

    @GetMapping
    public ResponseEntity<List<SpaceMission>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMission> getMissionById(@PathVariable Long id) {
        return missionService.getMissionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SpaceMission> createMission(@RequestBody SpaceMission mission) {
        SpaceMission saved = missionService.saveMission(mission);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceMission> updateMission(
            @PathVariable Long id,
            @RequestBody SpaceMission mission) {
        try {
            SpaceMission updated = missionService.updateMission(id, mission);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        if (missionService.getMissionById(id).isPresent()) {
            missionService.deleteMission(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== By Country ====================

    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<SpaceMission>> getMissionsByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(missionService.getMissionsByCountry(countryId));
    }

    @GetMapping("/by-country-code/{isoCode}")
    public ResponseEntity<List<SpaceMission>> getMissionsByCountryCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(missionService.getMissionsByCountryCode(isoCode));
    }

    @GetMapping("/by-country/{countryId}/status/{status}")
    public ResponseEntity<List<SpaceMission>> getMissionsByCountryAndStatus(
            @PathVariable Long countryId,
            @PathVariable MissionStatus status) {
        return ResponseEntity.ok(missionService.getMissionsByCountryAndStatus(countryId, status));
    }

    @GetMapping("/by-country/{countryId}/statistics")
    public ResponseEntity<Map<String, Object>> getCountryMissionStatistics(@PathVariable Long countryId) {
        return ResponseEntity.ok(missionService.getCountryMissionStatistics(countryId));
    }

    // ==================== By Status ====================

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<SpaceMission>> getMissionsByStatus(@PathVariable MissionStatus status) {
        return ResponseEntity.ok(missionService.getMissionsByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SpaceMission>> getActiveMissions() {
        return ResponseEntity.ok(missionService.getActiveMissions());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<SpaceMission>> getUpcomingMissions() {
        return ResponseEntity.ok(missionService.getUpcomingMissions());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<SpaceMission>> getCompletedMissions() {
        return ResponseEntity.ok(missionService.getCompletedMissions());
    }

    // ==================== By Mission Type ====================

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<SpaceMission>> getMissionsByType(@PathVariable MissionType type) {
        return ResponseEntity.ok(missionService.getMissionsByType(type));
    }

    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<SpaceMission>> getMissionsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(missionService.getMissionsByCategory(category));
    }

    // ==================== By Destination ====================

    @GetMapping("/by-destination/{destination}")
    public ResponseEntity<List<SpaceMission>> getMissionsByDestination(@PathVariable Destination destination) {
        return ResponseEntity.ok(missionService.getMissionsByDestination(destination));
    }

    @GetMapping("/by-destination/{destination}/successful")
    public ResponseEntity<List<SpaceMission>> getSuccessfulMissionsByDestination(@PathVariable Destination destination) {
        return ResponseEntity.ok(missionService.getSuccessfulMissionsByDestination(destination));
    }

    // ==================== Crewed Missions ====================

    @GetMapping("/crewed")
    public ResponseEntity<List<SpaceMission>> getCrewedMissions() {
        return ResponseEntity.ok(missionService.getCrewedMissions());
    }

    @GetMapping("/crewed/by-country/{countryId}")
    public ResponseEntity<List<SpaceMission>> getCrewedMissionsByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(missionService.getCrewedMissionsByCountry(countryId));
    }

    // ==================== Historic Firsts ====================

    @GetMapping("/historic-firsts")
    public ResponseEntity<List<SpaceMission>> getHistoricFirsts() {
        return ResponseEntity.ok(missionService.getHistoricFirsts());
    }

    @GetMapping("/historic-firsts/by-country/{countryId}")
    public ResponseEntity<List<SpaceMission>> getHistoricFirstsByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(missionService.getHistoricFirstsByCountry(countryId));
    }

    // ==================== Timeline ====================

    @GetMapping("/timeline")
    public ResponseEntity<List<SpaceMission>> getTimeline(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(missionService.getTimeline(startYear, endYear));
    }

    @GetMapping("/by-year/{year}")
    public ResponseEntity<List<SpaceMission>> getMissionsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(missionService.getMissionsByYear(year));
    }

    @GetMapping("/by-decade/{decade}")
    public ResponseEntity<List<SpaceMission>> getMissionsByDecade(@PathVariable Integer decade) {
        return ResponseEntity.ok(missionService.getMissionsByDecade(decade));
    }

    // ==================== Search ====================

    @GetMapping("/search")
    public ResponseEntity<List<SpaceMission>> searchMissions(@RequestParam String q) {
        return ResponseEntity.ok(missionService.searchMissions(q));
    }

    // ==================== Records ====================

    @GetMapping("/records/longest")
    public ResponseEntity<List<SpaceMission>> getLongestMissions(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(missionService.getLongestMissions(limit));
    }

    @GetMapping("/records/farthest")
    public ResponseEntity<List<SpaceMission>> getFarthestMissions(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(missionService.getFarthestMissions(limit));
    }

    @GetMapping("/records/largest-crew")
    public ResponseEntity<List<SpaceMission>> getLargestCrewMissions(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(missionService.getLargestCrewMissions(limit));
    }

    @GetMapping("/sample-return")
    public ResponseEntity<List<SpaceMission>> getSampleReturnMissions() {
        return ResponseEntity.ok(missionService.getSampleReturnMissions());
    }

    @GetMapping("/with-eva")
    public ResponseEntity<List<SpaceMission>> getMissionsWithEVA() {
        return ResponseEntity.ok(missionService.getMissionsWithEVA());
    }

    // ==================== Comparison ====================

    @GetMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareMissionsByCountries(
            @RequestParam List<Long> countries,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        return ResponseEntity.ok(missionService.compareMissionsByCountries(countries, startYear, endYear));
    }

    @GetMapping("/compare-by-destination")
    public ResponseEntity<Map<String, Object>> compareMissionsByDestination(
            @RequestParam Destination destination,
            @RequestParam List<Long> countries) {
        return ResponseEntity.ok(missionService.compareMissionsByDestination(destination, countries));
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMissionStatistics() {
        return ResponseEntity.ok(missionService.getMissionStatistics());
    }

    @GetMapping("/counts/by-country")
    public ResponseEntity<Map<String, Long>> getMissionCountsByCountry() {
        return ResponseEntity.ok(missionService.getMissionCountsByCountry());
    }

    @GetMapping("/counts/by-type")
    public ResponseEntity<Map<String, Long>> getMissionCountsByType() {
        return ResponseEntity.ok(missionService.getMissionCountsByType());
    }

    @GetMapping("/counts/by-destination")
    public ResponseEntity<Map<String, Long>> getMissionCountsByDestination() {
        return ResponseEntity.ok(missionService.getMissionCountsByDestination());
    }

    @GetMapping("/counts/by-year")
    public ResponseEntity<Map<Integer, Long>> getMissionCountsByYear() {
        return ResponseEntity.ok(missionService.getMissionCountsByYear());
    }

    // ==================== Meta Information ====================

    @GetMapping("/statuses")
    public ResponseEntity<List<MissionStatusInfo>> getMissionStatuses() {
        List<MissionStatusInfo> statuses = Arrays.stream(MissionStatus.values())
                .map(s -> new MissionStatusInfo(
                        s.name(),
                        s.getDisplayName(),
                        s.getDescription(),
                        s.isActive(),
                        s.isCompleted(),
                        s.isFailed(),
                        s.isPending()
                ))
                .toList();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/types")
    public ResponseEntity<List<MissionTypeInfo>> getMissionTypes() {
        List<MissionTypeInfo> types = Arrays.stream(MissionType.values())
                .map(t -> new MissionTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription(),
                        t.isCrewed(),
                        t.isDeepSpace()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/types/by-category/{category}")
    public ResponseEntity<List<MissionTypeInfo>> getMissionTypesByCategory(@PathVariable String category) {
        List<MissionTypeInfo> types = MissionType.getByCategory(category).stream()
                .map(t -> new MissionTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription(),
                        t.isCrewed(),
                        t.isDeepSpace()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/types/categories")
    public ResponseEntity<List<String>> getMissionTypeCategories() {
        return ResponseEntity.ok(MissionType.getAllCategories());
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<DestinationInfo>> getDestinations() {
        List<DestinationInfo> destinations = Arrays.stream(Destination.values())
                .map(d -> new DestinationInfo(
                        d.name(),
                        d.getDisplayName(),
                        d.getCategory(),
                        d.getTypicalDistanceKm(),
                        d.getDescription(),
                        d.isEarthOrbit(),
                        d.isLunar(),
                        d.isMars(),
                        d.isDeepSpace()
                ))
                .toList();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/destinations/by-category/{category}")
    public ResponseEntity<List<DestinationInfo>> getDestinationsByCategory(@PathVariable String category) {
        List<DestinationInfo> destinations = Destination.getByCategory(category).stream()
                .map(d -> new DestinationInfo(
                        d.name(),
                        d.getDisplayName(),
                        d.getCategory(),
                        d.getTypicalDistanceKm(),
                        d.getDescription(),
                        d.isEarthOrbit(),
                        d.isLunar(),
                        d.isMars(),
                        d.isDeepSpace()
                ))
                .toList();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/destinations/categories")
    public ResponseEntity<List<String>> getDestinationCategories() {
        return ResponseEntity.ok(Destination.getAllCategories());
    }

    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAllLaunchYears() {
        return ResponseEntity.ok(missionService.getAllLaunchYears());
    }

    @GetMapping("/decades")
    public ResponseEntity<List<Integer>> getAllLaunchDecades() {
        return ResponseEntity.ok(missionService.getAllLaunchDecades());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllMissionCategories() {
        return ResponseEntity.ok(missionService.getAllMissionCategories());
    }

    @GetMapping("/operators")
    public ResponseEntity<List<String>> getAllOperators() {
        return ResponseEntity.ok(missionService.getAllOperators());
    }

    @GetMapping("/launch-sites")
    public ResponseEntity<List<String>> getAllLaunchSites() {
        return ResponseEntity.ok(missionService.getAllLaunchSites());
    }

    // ==================== DTOs ====================

    public record MissionStatusInfo(
            String name,
            String displayName,
            String description,
            boolean isActive,
            boolean isCompleted,
            boolean isFailed,
            boolean isPending
    ) {}

    public record MissionTypeInfo(
            String name,
            String displayName,
            String category,
            String description,
            boolean isCrewed,
            boolean isDeepSpace
    ) {}

    public record DestinationInfo(
            String name,
            String displayName,
            String category,
            long typicalDistanceKm,
            String description,
            boolean isEarthOrbit,
            boolean isLunar,
            boolean isMars,
            boolean isDeepSpace
    ) {}
}
