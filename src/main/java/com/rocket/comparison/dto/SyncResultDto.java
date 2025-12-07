package com.rocket.comparison.dto;

import java.time.LocalDateTime;

/**
 * DTO for data synchronization operation results.
 */
public record SyncResultDto(
    int fetched,
    int created,
    int updated,
    int skipped,
    LocalDateTime syncedAt
) {
    public static SyncResultDto of(int fetched, int created, int updated, int skipped) {
        return new SyncResultDto(fetched, created, updated, skipped, LocalDateTime.now());
    }

    public int total() {
        return created + updated;
    }
}
