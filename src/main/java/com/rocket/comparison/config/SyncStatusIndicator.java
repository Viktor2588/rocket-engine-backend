package com.rocket.comparison.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Health indicator for tracking external API sync status (BE-080)
 */
@Component
public class SyncStatusIndicator implements HealthIndicator {

    private final AtomicReference<SyncStatus> lastSyncStatus = new AtomicReference<>(
            new SyncStatus(null, "NOT_STARTED", null, null)
    );

    @Override
    public Health health() {
        SyncStatus status = lastSyncStatus.get();

        Health.Builder builder = status.status.equals("SUCCESS") || status.status.equals("NOT_STARTED")
                ? Health.up()
                : Health.down();

        builder.withDetail("lastSyncTime", status.timestamp != null ? status.timestamp.toString() : "Never");
        builder.withDetail("status", status.status);

        if (status.recordsSynced != null) {
            builder.withDetail("recordsSynced", status.recordsSynced);
        }
        if (status.error != null) {
            builder.withDetail("error", status.error);
        }

        return builder.build();
    }

    /**
     * Record a successful sync
     */
    public void recordSyncSuccess(int recordsSynced) {
        lastSyncStatus.set(new SyncStatus(
                LocalDateTime.now(),
                "SUCCESS",
                recordsSynced,
                null
        ));
    }

    /**
     * Record a failed sync
     */
    public void recordSyncFailure(String error) {
        lastSyncStatus.set(new SyncStatus(
                LocalDateTime.now(),
                "FAILED",
                null,
                error
        ));
    }

    /**
     * Record sync in progress
     */
    public void recordSyncStarted() {
        lastSyncStatus.set(new SyncStatus(
                LocalDateTime.now(),
                "IN_PROGRESS",
                null,
                null
        ));
    }

    private record SyncStatus(
            LocalDateTime timestamp,
            String status,
            Integer recordsSynced,
            String error
    ) {}
}
