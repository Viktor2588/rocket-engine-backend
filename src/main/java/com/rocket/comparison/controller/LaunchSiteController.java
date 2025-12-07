package com.rocket.comparison.controller;

import com.rocket.comparison.entity.LaunchSite;
import com.rocket.comparison.entity.LaunchSiteStatus;
import com.rocket.comparison.service.LaunchSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/launch-sites")
@RequiredArgsConstructor
public class LaunchSiteController {

    private final LaunchSiteService launchSiteService;

    // ==================== Basic CRUD ====================

    @GetMapping
    public ResponseEntity<List<LaunchSite>> getAllLaunchSites() {
        return ResponseEntity.ok(launchSiteService.getAllLaunchSites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchSite> getLaunchSiteById(@PathVariable Long id) {
        return launchSiteService.getLaunchSiteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/short-name/{shortName}")
    public ResponseEntity<LaunchSite> getLaunchSiteByShortName(@PathVariable String shortName) {
        return launchSiteService.getLaunchSiteByShortName(shortName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LaunchSite> createLaunchSite(@RequestBody LaunchSite launchSite) {
        LaunchSite saved = launchSiteService.saveLaunchSite(launchSite);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaunchSite> updateLaunchSite(
            @PathVariable Long id,
            @RequestBody LaunchSite launchSite) {
        try {
            LaunchSite updated = launchSiteService.updateLaunchSite(id, launchSite);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaunchSite(@PathVariable Long id) {
        if (launchSiteService.getLaunchSiteById(id).isPresent()) {
            launchSiteService.deleteLaunchSite(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== By Country ====================

    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByCountry(countryId));
    }

    @GetMapping("/by-country-code/{isoCode}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByCountryCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByCountryCode(isoCode));
    }

    @GetMapping("/by-country/{countryId}/status/{status}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByCountryAndStatus(
            @PathVariable Long countryId,
            @PathVariable LaunchSiteStatus status) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByCountryAndStatus(countryId, status));
    }

    @GetMapping("/by-country/{countryId}/statistics")
    public ResponseEntity<Map<String, Object>> getCountryLaunchSiteStatistics(@PathVariable Long countryId) {
        return ResponseEntity.ok(launchSiteService.getCountryLaunchSiteStatistics(countryId));
    }

    // ==================== By Status ====================

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByStatus(@PathVariable LaunchSiteStatus status) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<LaunchSite>> getActiveLaunchSites() {
        return ResponseEntity.ok(launchSiteService.getActiveLaunchSites());
    }

    @GetMapping("/active/count")
    public ResponseEntity<Long> countActiveLaunchSites() {
        return ResponseEntity.ok(launchSiteService.countActiveLaunchSites());
    }

    // ==================== By Capabilities ====================

    @GetMapping("/human-rated")
    public ResponseEntity<List<LaunchSite>> getHumanRatedSites() {
        return ResponseEntity.ok(launchSiteService.getHumanRatedSites());
    }

    @GetMapping("/interplanetary-capable")
    public ResponseEntity<List<LaunchSite>> getInterplanetaryCapableSites() {
        return ResponseEntity.ok(launchSiteService.getInterplanetaryCapableSites());
    }

    @GetMapping("/geo-capable")
    public ResponseEntity<List<LaunchSite>> getGeoCapableSites() {
        return ResponseEntity.ok(launchSiteService.getGeoCapableSites());
    }

    @GetMapping("/polar-capable")
    public ResponseEntity<List<LaunchSite>> getPolarCapableSites() {
        return ResponseEntity.ok(launchSiteService.getPolarCapableSites());
    }

    @GetMapping("/with-landing")
    public ResponseEntity<List<LaunchSite>> getSitesWithLandingFacilities() {
        return ResponseEntity.ok(launchSiteService.getSitesWithLandingFacilities());
    }

    // ==================== By Location ====================

    @GetMapping("/by-latitude")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByLatitudeRange(
            @RequestParam Double minLatitude,
            @RequestParam Double maxLatitude) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByLatitudeRange(minLatitude, maxLatitude));
    }

    @GetMapping("/by-region/{region}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByRegion(@PathVariable String region) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByRegion(region));
    }

    @GetMapping("/regions")
    public ResponseEntity<List<String>> getAllRegions() {
        return ResponseEntity.ok(launchSiteService.getAllRegions());
    }

    @GetMapping("/geographic-distribution")
    public ResponseEntity<Map<String, Object>> getGeographicDistribution() {
        return ResponseEntity.ok(launchSiteService.getGeographicDistribution());
    }

    // ==================== By Operator ====================

    @GetMapping("/by-operator/{operator}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByOperator(@PathVariable String operator) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByOperator(operator));
    }

    @GetMapping("/operators")
    public ResponseEntity<List<String>> getAllOperators() {
        return ResponseEntity.ok(launchSiteService.getAllOperators());
    }

    // ==================== Rankings ====================

    @GetMapping("/rankings/most-launches")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByMostLaunches(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByMostLaunches(limit));
    }

    @GetMapping("/rankings/highest-success-rate")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByHighestSuccessRate(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByHighestSuccessRate(limit));
    }

    // ==================== Timeline ====================

    @GetMapping("/by-established-year/{year}")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByEstablishedYear(@PathVariable Integer year) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByEstablishedYear(year));
    }

    @GetMapping("/by-established-year-range")
    public ResponseEntity<List<LaunchSite>> getLaunchSitesByEstablishedYearRange(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        return ResponseEntity.ok(launchSiteService.getLaunchSitesByEstablishedYearRange(startYear, endYear));
    }

    @GetMapping("/established-years")
    public ResponseEntity<List<Integer>> getAllEstablishedYears() {
        return ResponseEntity.ok(launchSiteService.getAllEstablishedYears());
    }

    // ==================== Search ====================

    @GetMapping("/search")
    public ResponseEntity<List<LaunchSite>> searchLaunchSites(@RequestParam String q) {
        return ResponseEntity.ok(launchSiteService.searchLaunchSites(q));
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getGlobalStatistics() {
        return ResponseEntity.ok(launchSiteService.getGlobalStatistics());
    }

    @GetMapping("/counts/by-country")
    public ResponseEntity<Map<String, Long>> getLaunchSiteCountsByCountry() {
        return ResponseEntity.ok(launchSiteService.getLaunchSiteCountsByCountry());
    }

    // ==================== Meta Information ====================

    @GetMapping("/statuses")
    public ResponseEntity<List<LaunchSiteStatusInfo>> getLaunchSiteStatuses() {
        List<LaunchSiteStatusInfo> statuses = Arrays.stream(LaunchSiteStatus.values())
                .map(s -> new LaunchSiteStatusInfo(
                        s.name(),
                        s.getDisplayName(),
                        s.getDescription(),
                        s.isActive(),
                        s.canLaunch()
                ))
                .toList();
        return ResponseEntity.ok(statuses);
    }

    // ==================== DTOs ====================

    public record LaunchSiteStatusInfo(
            String name,
            String displayName,
            String description,
            boolean isActive,
            boolean canLaunch
    ) {}
}
