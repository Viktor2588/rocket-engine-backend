package com.rocket.comparison.config;

import com.rocket.comparison.config.seeder.*;
import com.rocket.comparison.integration.spacedevs.SpaceDevsSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Orchestrates database seeding on application startup.
 * Delegates to individual EntitySeeder implementations for each entity type.
 * Only seeds data when the database is empty.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)  // Run before other CommandLineRunners
public class DataSeeder implements CommandLineRunner {

    // Entity seeders (ordered by dependency)
    private final CountrySeeder countrySeeder;
    private final EngineSeeder engineSeeder;
    private final LaunchVehicleSeeder launchVehicleSeeder;
    private final SpaceMilestoneSeeder spaceMilestoneSeeder;
    private final SpaceMissionSeeder spaceMissionSeeder;
    private final SatelliteSeeder satelliteSeeder;
    private final LaunchSiteSeeder launchSiteSeeder;
    private final CapabilityScoreSeeder capabilityScoreSeeder;

    // External API sync
    private final SpaceDevsSyncService spaceDevsSyncService;
    private final SyncStatusIndicator syncStatusIndicator;

    @Value("${sync.external.enabled:true}")
    private boolean externalSyncEnabled;

    @Value("${sync.external.missions-limit:200}")
    private int missionsLimit;

    @Value("${sync.external.sites-limit:100}")
    private int sitesLimit;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database for missing seed data...");

        // Seed each entity type independently if missing
        // Countries must be seeded first as other entities depend on them
        countrySeeder.seedIfEmpty();
        engineSeeder.seedIfEmpty();
        launchVehicleSeeder.seedIfEmpty();
        spaceMilestoneSeeder.seedIfEmpty();
        spaceMissionSeeder.seedIfEmpty();
        satelliteSeeder.seedIfEmpty();
        launchSiteSeeder.seedIfEmpty();
        capabilityScoreSeeder.seedIfEmpty();

        log.info("Database check completed!");
        log.info("Current counts: {} countries, {} engines, {} launch vehicles, {} milestones, {} missions, {} satellites, {} launch sites",
            countrySeeder.count(),
            engineSeeder.count(),
            launchVehicleSeeder.count(),
            spaceMilestoneSeeder.count(),
            spaceMissionSeeder.count(),
            satelliteSeeder.count(),
            launchSiteSeeder.count()
        );

        // Sync real data from TheSpaceDevs API after seeding
        syncFromExternalApi();
    }

    /**
     * Syncs additional data from TheSpaceDevs API.
     * Runs after initial seeding to enrich the database with real launch data.
     * Can be disabled via sync.external.enabled=false property (BE-060).
     * Records sync status to database for persistence across restarts (BE-061).
     */
    private void syncFromExternalApi() {
        if (!externalSyncEnabled) {
            log.info("External API sync is disabled (sync.external.enabled=false)");
            return;
        }

        // Start tracking sync status
        var syncStatus = syncStatusIndicator.startSync("spacedevs_api");
        int totalRecordsSynced = 0;

        try {
            log.info("Starting automatic sync from TheSpaceDevs API (missions={}, sites={})...",
                missionsLimit, sitesLimit);

            // Sync recent launches (past missions)
            var missionResults = spaceDevsSyncService.syncRecentLaunches(missionsLimit);
            log.info("Mission sync completed: {}", missionResults);
            totalRecordsSynced += extractSyncedCount(missionResults);

            // Sync launch sites
            var siteResults = spaceDevsSyncService.syncLaunchSites(sitesLimit);
            log.info("Launch site sync completed: {}", siteResults);
            totalRecordsSynced += extractSyncedCount(siteResults);

            // Record successful sync
            syncStatusIndicator.recordSyncSuccess(syncStatus, totalRecordsSynced);
            log.info("External API sync completed successfully! Total records synced: {}", totalRecordsSynced);
        } catch (Exception e) {
            // Record failed sync
            syncStatusIndicator.recordSyncFailure(syncStatus, e.getMessage());
            log.warn("External API sync failed (non-critical): {}", e.getMessage());
            // Don't fail startup if external API is unavailable
        }
    }

    /**
     * Extracts the number of synced records from a sync result map.
     */
    private int extractSyncedCount(Map<String, Object> result) {
        if (result == null) return 0;
        Object synced = result.get("synced");
        if (synced instanceof Number) {
            return ((Number) synced).intValue();
        }
        return 0;
    }
}
