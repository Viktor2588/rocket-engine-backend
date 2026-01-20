package com.rocket.comparison.controller;

import com.rocket.comparison.config.seeder.*;
import com.rocket.comparison.integration.spacedevs.SpaceDevsSyncService;
import com.rocket.comparison.integration.truthledger.TruthLedgerSyncService;
import com.rocket.comparison.repository.EngineRepository;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing data synchronization with external APIs.
 * Provides manual triggers for syncing data from TheSpaceDevs API.
 * Also provides reseed endpoints for refreshing seed data.
 */
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Slf4j
public class DataSyncController {

    private final SpaceDevsSyncService syncService;
    private final TruthLedgerSyncService truthLedgerSyncService;

    // Repositories for clearing data
    private final EngineRepository engineRepository;
    private final LaunchVehicleRepository launchVehicleRepository;

    // Seeders for reseeding
    private final EngineSeeder engineSeeder;
    private final LaunchVehicleSeeder launchVehicleSeeder;
    private final CountrySeeder countrySeeder;
    private final SpaceMilestoneSeeder spaceMilestoneSeeder;
    private final SpaceMissionSeeder spaceMissionSeeder;
    private final SatelliteSeeder satelliteSeeder;
    private final LaunchSiteSeeder launchSiteSeeder;
    private final CapabilityScoreSeeder capabilityScoreSeeder;

    /**
     * Trigger a full sync from TheSpaceDevs API.
     * Syncs launch sites and recent missions.
     *
     * POST /api/sync/full
     */
    @PostMapping("/full")
    public ResponseEntity<Map<String, Object>> fullSync() {
        log.info("Manual full sync triggered");
        Map<String, Object> results = syncService.fullSync();
        return ResponseEntity.ok(results);
    }

    /**
     * Sync recent launches/missions only.
     *
     * POST /api/sync/missions?limit=100
     */
    @PostMapping("/missions")
    public ResponseEntity<Map<String, Object>> syncMissions(
            @RequestParam(defaultValue = "100") int limit) {
        log.info("Manual mission sync triggered with limit: {}", limit);
        Map<String, Object> results = syncService.syncRecentLaunches(limit);
        return ResponseEntity.ok(results);
    }

    /**
     * Sync upcoming launches/missions.
     *
     * POST /api/sync/upcoming?limit=50
     */
    @PostMapping("/upcoming")
    public ResponseEntity<Map<String, Object>> syncUpcoming(
            @RequestParam(defaultValue = "50") int limit) {
        log.info("Manual upcoming launch sync triggered with limit: {}", limit);
        Map<String, Object> results = syncService.syncUpcomingLaunches(limit);
        return ResponseEntity.ok(results);
    }

    /**
     * Sync launch sites from pads.
     *
     * POST /api/sync/launch-sites?limit=100
     */
    @PostMapping("/launch-sites")
    public ResponseEntity<Map<String, Object>> syncLaunchSites(
            @RequestParam(defaultValue = "100") int limit) {
        log.info("Manual launch site sync triggered with limit: {}", limit);
        Map<String, Object> results = syncService.syncLaunchSites(limit);
        return ResponseEntity.ok(results);
    }

    /**
     * Get sync status and available endpoints.
     *
     * GET /api/sync
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSyncInfo() {
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("POST /api/sync/full", "Full sync (missions + launch sites)");
        endpoints.put("POST /api/sync/missions", "Sync recent missions (param: limit)");
        endpoints.put("POST /api/sync/upcoming", "Sync upcoming launches (param: limit)");
        endpoints.put("POST /api/sync/launch-sites", "Sync launch sites (param: limit)");
        endpoints.put("POST /api/sync/reseed/engines", "Clear and reseed all engines");
        endpoints.put("POST /api/sync/reseed/launch-vehicles", "Clear and reseed launch vehicles");
        endpoints.put("POST /api/sync/reseed/all", "Clear and reseed all seed data");
        endpoints.put("POST /api/sync/truth-ledger/engines", "Sync engines from Truth Ledger");
        endpoints.put("POST /api/sync/truth-ledger/launch-vehicles", "Sync launch vehicles from Truth Ledger");
        endpoints.put("POST /api/sync/truth-ledger/launch-sites", "Sync launch sites from Truth Ledger");
        endpoints.put("POST /api/sync/truth-ledger/missions", "Sync space missions from Truth Ledger");
        endpoints.put("POST /api/sync/truth-ledger/all", "Sync all entities from Truth Ledger");

        return ResponseEntity.ok(Map.of(
            "description", "Data synchronization and seeding management",
            "source", "https://thespacedevs.com/llapi",
            "endpoints", endpoints,
            "note", "Reseed endpoints will clear and re-import data"
        ));
    }

    // ==================== Reseed Endpoints ====================

    /**
     * Clear and reseed all engines.
     * WARNING: This will delete all existing engine data!
     *
     * POST /api/sync/reseed/engines
     */
    @PostMapping("/reseed/engines")
    @Transactional
    public ResponseEntity<Map<String, Object>> reseedEngines() {
        log.warn("Engine reseed triggered - clearing existing data");

        long deletedCount = engineRepository.count();
        engineRepository.deleteAll();
        log.info("Deleted {} existing engines", deletedCount);

        engineSeeder.seedIfEmpty();
        long newCount = engineRepository.count();

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "deleted", deletedCount,
            "seeded", newCount,
            "message", String.format("Reseeded %d engines (deleted %d)", newCount, deletedCount)
        ));
    }

    /**
     * Clear and reseed all launch vehicles.
     * WARNING: This will delete all existing launch vehicle data!
     *
     * POST /api/sync/reseed/launch-vehicles
     */
    @PostMapping("/reseed/launch-vehicles")
    @Transactional
    public ResponseEntity<Map<String, Object>> reseedLaunchVehicles() {
        log.warn("Launch vehicle reseed triggered - clearing existing data");

        long deletedCount = launchVehicleRepository.count();
        launchVehicleRepository.deleteAll();
        log.info("Deleted {} existing launch vehicles", deletedCount);

        launchVehicleSeeder.seedIfEmpty();
        long newCount = launchVehicleRepository.count();

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "deleted", deletedCount,
            "seeded", newCount,
            "message", String.format("Reseeded %d launch vehicles (deleted %d)", newCount, deletedCount)
        ));
    }

    /**
     * Clear and reseed all data (engines, launch vehicles, etc.)
     * WARNING: This will delete all existing seed data!
     *
     * POST /api/sync/reseed/all
     */
    @PostMapping("/reseed/all")
    @Transactional
    public ResponseEntity<Map<String, Object>> reseedAll() {
        log.warn("Full reseed triggered - clearing all seed data");

        Map<String, Object> results = new HashMap<>();

        // Clear and reseed engines
        long enginesBefore = engineRepository.count();
        engineRepository.deleteAll();
        engineSeeder.seedIfEmpty();
        results.put("engines", Map.of("deleted", enginesBefore, "seeded", engineRepository.count()));

        // Clear and reseed launch vehicles
        long vehiclesBefore = launchVehicleRepository.count();
        launchVehicleRepository.deleteAll();
        launchVehicleSeeder.seedIfEmpty();
        results.put("launchVehicles", Map.of("deleted", vehiclesBefore, "seeded", launchVehicleRepository.count()));

        // Reseed other entities (only if empty - they use seedIfEmpty)
        countrySeeder.seedIfEmpty();
        spaceMilestoneSeeder.seedIfEmpty();
        spaceMissionSeeder.seedIfEmpty();
        satelliteSeeder.seedIfEmpty();
        launchSiteSeeder.seedIfEmpty();
        capabilityScoreSeeder.seedIfEmpty();

        results.put("status", "success");
        results.put("message", "Full reseed completed");

        return ResponseEntity.ok(results);
    }

    // ==================== Truth Ledger Sync Endpoints ====================

    /**
     * Sync engines from Truth Ledger.
     * This fetches entities and their verified facts from Truth Ledger
     * and creates/updates corresponding engine entities in the database.
     *
     * POST /api/sync/truth-ledger/engines
     */
    @PostMapping("/truth-ledger/engines")
    public ResponseEntity<Map<String, Object>> syncEnginesFromTruthLedger() {
        log.info("Truth Ledger engine sync triggered");
        Map<String, Object> results = truthLedgerSyncService.syncEngines();
        return ResponseEntity.ok(results);
    }

    /**
     * Sync launch vehicles from Truth Ledger.
     * This fetches entities and their verified facts from Truth Ledger
     * and creates/updates corresponding launch vehicle entities in the database.
     *
     * POST /api/sync/truth-ledger/launch-vehicles
     */
    @PostMapping("/truth-ledger/launch-vehicles")
    public ResponseEntity<Map<String, Object>> syncLaunchVehiclesFromTruthLedger() {
        log.info("Truth Ledger launch vehicle sync triggered");
        Map<String, Object> results = truthLedgerSyncService.syncLaunchVehicles();
        return ResponseEntity.ok(results);
    }

    /**
     * Sync launch sites from Truth Ledger.
     * This fetches entities and their verified facts from Truth Ledger
     * and creates/updates corresponding launch site entities in the database.
     *
     * POST /api/sync/truth-ledger/launch-sites
     */
    @PostMapping("/truth-ledger/launch-sites")
    public ResponseEntity<Map<String, Object>> syncLaunchSitesFromTruthLedger() {
        log.info("Truth Ledger launch site sync triggered");
        Map<String, Object> results = truthLedgerSyncService.syncLaunchSites();
        return ResponseEntity.ok(results);
    }

    /**
     * Sync space missions from Truth Ledger.
     * This fetches entities and their verified facts from Truth Ledger
     * and creates/updates corresponding space mission entities in the database.
     *
     * POST /api/sync/truth-ledger/missions
     */
    @PostMapping("/truth-ledger/missions")
    public ResponseEntity<Map<String, Object>> syncSpaceMissionsFromTruthLedger() {
        log.info("Truth Ledger space mission sync triggered");
        Map<String, Object> results = truthLedgerSyncService.syncSpaceMissions();
        return ResponseEntity.ok(results);
    }

    /**
     * Sync all entities from Truth Ledger.
     * This fetches all supported entity types (engines, launch vehicles)
     * from Truth Ledger and syncs them to the database.
     *
     * POST /api/sync/truth-ledger/all
     */
    @PostMapping("/truth-ledger/all")
    public ResponseEntity<Map<String, Object>> syncAllFromTruthLedger() {
        log.info("Full Truth Ledger sync triggered");
        Map<String, Object> results = truthLedgerSyncService.syncAll();
        return ResponseEntity.ok(results);
    }
}
