package com.rocket.comparison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity to persist sync status across application restarts (BE-061)
 */
@Entity
@Table(name = "sync_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String syncType; // e.g., "missions", "launch_sites", "full"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncState state; // SUCCESS, FAILED, IN_PROGRESS

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private Integer recordsSynced;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private String sourceApi; // e.g., "TheSpaceDevs"

    public enum SyncState {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }

    // Constructor for starting a sync
    public static SyncStatus startSync(String syncType, String sourceApi) {
        SyncStatus status = new SyncStatus();
        status.setSyncType(syncType);
        status.setSourceApi(sourceApi);
        status.setState(SyncState.IN_PROGRESS);
        status.setStartedAt(LocalDateTime.now());
        return status;
    }

    // Mark as successful
    public void markSuccess(int recordsSynced) {
        this.state = SyncState.SUCCESS;
        this.recordsSynced = recordsSynced;
        this.completedAt = LocalDateTime.now();
    }

    // Mark as failed
    public void markFailed(String errorMessage) {
        this.state = SyncState.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }
}
