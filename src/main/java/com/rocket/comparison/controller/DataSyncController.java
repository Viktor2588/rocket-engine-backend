package com.rocket.comparison.controller;

import com.rocket.comparison.integration.spacedevs.SpaceDevsSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing data synchronization with external APIs.
 * Provides manual triggers for syncing data from TheSpaceDevs API.
 */
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Slf4j
public class DataSyncController {

    private final SpaceDevsSyncService syncService;

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
        return ResponseEntity.ok(Map.of(
            "description", "Data synchronization with TheSpaceDevs API",
            "source", "https://thespacedevs.com/llapi",
            "endpoints", Map.of(
                "POST /api/sync/full", "Full sync (missions + launch sites)",
                "POST /api/sync/missions", "Sync recent missions (param: limit)",
                "POST /api/sync/upcoming", "Sync upcoming launches (param: limit)",
                "POST /api/sync/launch-sites", "Sync launch sites (param: limit)"
            ),
            "note", "Data is merged with existing records, not replaced"
        ));
    }
}
