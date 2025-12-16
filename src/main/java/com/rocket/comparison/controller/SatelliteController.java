package com.rocket.comparison.controller;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.service.SatelliteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/satellites")
@RequiredArgsConstructor
public class SatelliteController {

    private final SatelliteService satelliteService;

    // ==================== Basic CRUD ====================

    @GetMapping
    public ResponseEntity<List<Satellite>> getAllSatellites() {
        return ResponseEntity.ok(satelliteService.getAllSatellites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Satellite> getSatelliteById(@PathVariable Long id) {
        return satelliteService.getSatelliteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/norad/{noradId}")
    public ResponseEntity<Satellite> getSatelliteByNoradId(@PathVariable String noradId) {
        return satelliteService.getSatelliteByNoradId(noradId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cospar/{cosparId}")
    public ResponseEntity<Satellite> getSatelliteByCosparId(@PathVariable String cosparId) {
        return satelliteService.getSatelliteByCosparId(cosparId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Satellite> createSatellite(@Valid @RequestBody Satellite satellite) {
        Satellite saved = satelliteService.saveSatellite(satellite);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Satellite> updateSatellite(
            @PathVariable Long id,
            @Valid @RequestBody Satellite satellite) {
        try {
            Satellite updated = satelliteService.updateSatellite(id, satellite);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSatellite(@PathVariable Long id) {
        if (satelliteService.getSatelliteById(id).isPresent()) {
            satelliteService.deleteSatellite(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== By Country ====================

    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<Satellite>> getSatellitesByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(satelliteService.getSatellitesByCountry(countryId));
    }

    @GetMapping("/by-country-code/{isoCode}")
    public ResponseEntity<List<Satellite>> getSatellitesByCountryCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(satelliteService.getSatellitesByCountryCode(isoCode));
    }

    @GetMapping("/by-country/{countryId}/status/{status}")
    public ResponseEntity<List<Satellite>> getSatellitesByCountryAndStatus(
            @PathVariable Long countryId,
            @PathVariable SatelliteStatus status) {
        return ResponseEntity.ok(satelliteService.getSatellitesByCountryAndStatus(countryId, status));
    }

    @GetMapping("/by-country/{countryId}/statistics")
    public ResponseEntity<Map<String, Object>> getCountrySatelliteStatistics(@PathVariable Long countryId) {
        return ResponseEntity.ok(satelliteService.getCountrySatelliteStatistics(countryId));
    }

    // ==================== By Status ====================

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<Satellite>> getSatellitesByStatus(@PathVariable SatelliteStatus status) {
        return ResponseEntity.ok(satelliteService.getSatellitesByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Satellite>> getActiveSatellites() {
        return ResponseEntity.ok(satelliteService.getActiveSatellites());
    }

    @GetMapping("/active/count")
    public ResponseEntity<Long> countActiveSatellites() {
        return ResponseEntity.ok(satelliteService.countActiveSatellites());
    }

    // ==================== By Type ====================

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<Satellite>> getSatellitesByType(@PathVariable SatelliteType type) {
        return ResponseEntity.ok(satelliteService.getSatellitesByType(type));
    }

    // ==================== By Orbit ====================

    @GetMapping("/by-orbit/{orbitType}")
    public ResponseEntity<List<Satellite>> getSatellitesByOrbitType(@PathVariable OrbitType orbitType) {
        return ResponseEntity.ok(satelliteService.getSatellitesByOrbitType(orbitType));
    }

    @GetMapping("/by-altitude")
    public ResponseEntity<List<Satellite>> getSatellitesByAltitudeRange(
            @RequestParam Double minAltitude,
            @RequestParam Double maxAltitude) {
        return ResponseEntity.ok(satelliteService.getSatellitesByAltitudeRange(minAltitude, maxAltitude));
    }

    @GetMapping("/geostationary")
    public ResponseEntity<List<Satellite>> getGeostationarySatellites() {
        return ResponseEntity.ok(satelliteService.getGeostationarySatellites());
    }

    // ==================== By Constellation ====================

    @GetMapping("/by-constellation/{constellation}")
    public ResponseEntity<List<Satellite>> getSatellitesByConstellation(@PathVariable String constellation) {
        return ResponseEntity.ok(satelliteService.getSatellitesByConstellation(constellation));
    }

    @GetMapping("/constellations")
    public ResponseEntity<List<String>> getAllConstellations() {
        return ResponseEntity.ok(satelliteService.getAllConstellations());
    }

    @GetMapping("/constellation/{constellation}/count")
    public ResponseEntity<Long> countByConstellation(@PathVariable String constellation) {
        return ResponseEntity.ok(satelliteService.countByConstellation(constellation));
    }

    @GetMapping("/constellation/{constellation}/analysis")
    public ResponseEntity<Map<String, Object>> getConstellationAnalysis(@PathVariable String constellation) {
        return ResponseEntity.ok(satelliteService.getConstellationAnalysis(constellation));
    }

    // ==================== By Operator ====================

    @GetMapping("/by-operator/{operator}")
    public ResponseEntity<List<Satellite>> getSatellitesByOperator(@PathVariable String operator) {
        return ResponseEntity.ok(satelliteService.getSatellitesByOperator(operator));
    }

    @GetMapping("/operators")
    public ResponseEntity<List<String>> getAllOperators() {
        return ResponseEntity.ok(satelliteService.getAllOperators());
    }

    // ==================== Special Categories ====================

    @GetMapping("/navigation")
    public ResponseEntity<List<Satellite>> getNavigationSatellites() {
        return ResponseEntity.ok(satelliteService.getNavigationSatellites());
    }

    @GetMapping("/space-stations")
    public ResponseEntity<List<Satellite>> getSpaceStations() {
        return ResponseEntity.ok(satelliteService.getSpaceStations());
    }

    // ==================== Timeline ====================

    @GetMapping("/by-year/{year}")
    public ResponseEntity<List<Satellite>> getSatellitesByLaunchYear(@PathVariable Integer year) {
        return ResponseEntity.ok(satelliteService.getSatellitesByLaunchYear(year));
    }

    @GetMapping("/by-year-range")
    public ResponseEntity<List<Satellite>> getSatellitesByLaunchYearRange(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        return ResponseEntity.ok(satelliteService.getSatellitesByLaunchYearRange(startYear, endYear));
    }

    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAllLaunchYears() {
        return ResponseEntity.ok(satelliteService.getAllLaunchYears());
    }

    // ==================== Search ====================

    @GetMapping("/search")
    public ResponseEntity<List<Satellite>> searchSatellites(@RequestParam String q) {
        return ResponseEntity.ok(satelliteService.searchSatellites(q));
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getGlobalStatistics() {
        return ResponseEntity.ok(satelliteService.getGlobalStatistics());
    }

    @GetMapping("/counts/by-country")
    public ResponseEntity<Map<String, Long>> getSatelliteCountsByCountry() {
        return ResponseEntity.ok(satelliteService.getSatelliteCountsByCountry());
    }

    @GetMapping("/counts/by-type")
    public ResponseEntity<Map<String, Long>> getSatelliteCountsByType() {
        return ResponseEntity.ok(satelliteService.getSatelliteCountsByType());
    }

    @GetMapping("/counts/by-orbit")
    public ResponseEntity<Map<String, Long>> getSatelliteCountsByOrbit() {
        return ResponseEntity.ok(satelliteService.getSatelliteCountsByOrbit());
    }

    @GetMapping("/counts/by-constellation")
    public ResponseEntity<Map<String, Long>> getSatelliteCountsByConstellation() {
        return ResponseEntity.ok(satelliteService.getSatelliteCountsByConstellation());
    }

    @GetMapping("/counts/by-year")
    public ResponseEntity<Map<Integer, Long>> getSatelliteCountsByYear() {
        return ResponseEntity.ok(satelliteService.getSatelliteCountsByYear());
    }

    // ==================== Meta Information ====================

    @GetMapping("/types")
    public ResponseEntity<List<SatelliteTypeInfo>> getSatelliteTypes() {
        List<SatelliteTypeInfo> types = Arrays.stream(SatelliteType.values())
                .map(t -> new SatelliteTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/types/by-category/{category}")
    public ResponseEntity<List<SatelliteTypeInfo>> getSatelliteTypesByCategory(@PathVariable String category) {
        List<SatelliteTypeInfo> types = SatelliteType.getByCategory(category).stream()
                .map(t -> new SatelliteTypeInfo(
                        t.name(),
                        t.getDisplayName(),
                        t.getCategory(),
                        t.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/types/categories")
    public ResponseEntity<List<String>> getSatelliteTypeCategories() {
        return ResponseEntity.ok(SatelliteType.getAllCategories());
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<SatelliteStatusInfo>> getSatelliteStatuses() {
        List<SatelliteStatusInfo> statuses = Arrays.stream(SatelliteStatus.values())
                .map(s -> new SatelliteStatusInfo(
                        s.name(),
                        s.getDisplayName(),
                        s.getDescription(),
                        s.isActive(),
                        s.isInSpace(),
                        s.isTerminated()
                ))
                .toList();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/orbits")
    public ResponseEntity<List<OrbitTypeInfo>> getOrbitTypes() {
        List<OrbitTypeInfo> orbits = Arrays.stream(OrbitType.values())
                .map(o -> new OrbitTypeInfo(
                        o.name(),
                        o.getDisplayName(),
                        o.getCategory(),
                        o.getMinAltitudeKm(),
                        o.getMaxAltitudeKm(),
                        o.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(orbits);
    }

    @GetMapping("/orbits/by-category/{category}")
    public ResponseEntity<List<OrbitTypeInfo>> getOrbitTypesByCategory(@PathVariable String category) {
        List<OrbitTypeInfo> orbits = OrbitType.getByCategory(category).stream()
                .map(o -> new OrbitTypeInfo(
                        o.name(),
                        o.getDisplayName(),
                        o.getCategory(),
                        o.getMinAltitudeKm(),
                        o.getMaxAltitudeKm(),
                        o.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(orbits);
    }

    @GetMapping("/orbits/categories")
    public ResponseEntity<List<String>> getOrbitCategories() {
        return ResponseEntity.ok(OrbitType.getAllCategories());
    }

    // ==================== DTOs ====================

    public record SatelliteTypeInfo(
            String name,
            String displayName,
            String category,
            String description
    ) {}

    public record SatelliteStatusInfo(
            String name,
            String displayName,
            String description,
            boolean isActive,
            boolean isInSpace,
            boolean isTerminated
    ) {}

    public record OrbitTypeInfo(
            String name,
            String displayName,
            String category,
            int minAltitudeKm,
            int maxAltitudeKm,
            String description
    ) {}
}
