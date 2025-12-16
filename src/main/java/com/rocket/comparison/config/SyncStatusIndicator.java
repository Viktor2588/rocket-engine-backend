package com.rocket.comparison.config;

import com.rocket.comparison.entity.SyncStatus;
import com.rocket.comparison.repository.SyncStatusRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Health indicator for tracking external API sync status (BE-080, BE-061)
 * Persists sync status to database for survival across restarts
 */
@Component
public class SyncStatusIndicator implements HealthIndicator {

    private static final String DEFAULT_SYNC_TYPE = "spacedevs_api";
    private static final String DEFAULT_SOURCE_API = "TheSpaceDevs";

    private final SyncStatusRepository syncStatusRepository;

    public SyncStatusIndicator(SyncStatusRepository syncStatusRepository) {
        this.syncStatusRepository = syncStatusRepository;
    }

    @Override
    public Health health() {
        Optional<SyncStatus> latestSync = syncStatusRepository.findLatestBySyncType(DEFAULT_SYNC_TYPE);

        if (latestSync.isEmpty()) {
            return Health.up()
                    .withDetail("lastSyncTime", "Never")
                    .withDetail("status", "NOT_STARTED")
                    .build();
        }

        SyncStatus status = latestSync.get();
        Health.Builder builder;

        switch (status.getState()) {
            case SUCCESS -> builder = Health.up();
            case IN_PROGRESS -> builder = Health.up(); // Still healthy while syncing
            case FAILED -> builder = Health.down();
            default -> builder = Health.unknown();
        }

        builder.withDetail("lastSyncTime", status.getStartedAt().toString());
        builder.withDetail("status", status.getState().name());

        if (status.getCompletedAt() != null) {
            builder.withDetail("completedAt", status.getCompletedAt().toString());
        }
        if (status.getRecordsSynced() != null) {
            builder.withDetail("recordsSynced", status.getRecordsSynced());
        }
        if (status.getErrorMessage() != null) {
            builder.withDetail("error", status.getErrorMessage());
        }

        // Add sync history summary
        List<SyncStatus> recentSyncs = syncStatusRepository.findRecentSyncs(5);
        long successCount = recentSyncs.stream()
                .filter(s -> s.getState() == SyncStatus.SyncState.SUCCESS)
                .count();
        builder.withDetail("recentSuccessRate", successCount + "/" + recentSyncs.size());

        return builder.build();
    }

    /**
     * Start a new sync operation
     * @param syncType the type of sync (e.g., "missions", "launch_sites", "spacedevs_api")
     * @return the created SyncStatus entity
     */
    public SyncStatus startSync(String syncType) {
        SyncStatus status = SyncStatus.startSync(syncType, DEFAULT_SOURCE_API);
        return syncStatusRepository.save(status);
    }

    /**
     * Start the default sync operation
     * @return the created SyncStatus entity
     */
    public SyncStatus startSync() {
        return startSync(DEFAULT_SYNC_TYPE);
    }

    /**
     * Record a successful sync
     * @param syncStatus the sync status to update
     * @param recordsSynced number of records synced
     */
    public void recordSyncSuccess(SyncStatus syncStatus, int recordsSynced) {
        syncStatus.markSuccess(recordsSynced);
        syncStatusRepository.save(syncStatus);
    }

    /**
     * Record a failed sync
     * @param syncStatus the sync status to update
     * @param error the error message
     */
    public void recordSyncFailure(SyncStatus syncStatus, String error) {
        syncStatus.markFailed(error);
        syncStatusRepository.save(syncStatus);
    }

    /**
     * Get the latest sync status for a given type
     */
    public Optional<SyncStatus> getLatestSync(String syncType) {
        return syncStatusRepository.findLatestBySyncType(syncType);
    }

    /**
     * Get the latest successful sync for a given type
     */
    public Optional<SyncStatus> getLatestSuccessfulSync(String syncType) {
        return syncStatusRepository.findLatestSuccessfulBySyncType(syncType);
    }

    /**
     * Check if any sync is currently in progress
     */
    public boolean isSyncInProgress() {
        return syncStatusRepository.isAnySyncInProgress();
    }

    /**
     * Get recent sync history
     */
    public List<SyncStatus> getRecentSyncs(int limit) {
        return syncStatusRepository.findRecentSyncs(limit);
    }
}
