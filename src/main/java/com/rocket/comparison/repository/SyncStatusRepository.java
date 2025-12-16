package com.rocket.comparison.repository;

import com.rocket.comparison.entity.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for SyncStatus entity (BE-061)
 */
@Repository
public interface SyncStatusRepository extends JpaRepository<SyncStatus, Long> {

    // Get most recent sync for a given type
    @Query("SELECT s FROM SyncStatus s WHERE s.syncType = :syncType ORDER BY s.startedAt DESC LIMIT 1")
    Optional<SyncStatus> findLatestBySyncType(@Param("syncType") String syncType);

    // Get all syncs for a given type, ordered by most recent
    List<SyncStatus> findBySyncTypeOrderByStartedAtDesc(String syncType);

    // Get most recent successful sync for a type
    @Query("SELECT s FROM SyncStatus s WHERE s.syncType = :syncType AND s.state = 'SUCCESS' ORDER BY s.completedAt DESC LIMIT 1")
    Optional<SyncStatus> findLatestSuccessfulBySyncType(@Param("syncType") String syncType);

    // Get all in-progress syncs
    @Query("SELECT s FROM SyncStatus s WHERE s.state = 'IN_PROGRESS' ORDER BY s.startedAt DESC")
    List<SyncStatus> findAllInProgress();

    // Get failed syncs since a given time
    @Query("SELECT s FROM SyncStatus s WHERE s.state = 'FAILED' AND s.completedAt >= :since ORDER BY s.completedAt DESC")
    List<SyncStatus> findFailedSince(@Param("since") LocalDateTime since);

    // Get sync history (last N entries)
    @Query("SELECT s FROM SyncStatus s ORDER BY s.startedAt DESC LIMIT :limit")
    List<SyncStatus> findRecentSyncs(@Param("limit") int limit);

    // Count syncs by state
    @Query("SELECT s.state, COUNT(s) FROM SyncStatus s GROUP BY s.state")
    List<Object[]> countByState();

    // Check if any sync is currently in progress
    @Query("SELECT COUNT(s) > 0 FROM SyncStatus s WHERE s.state = 'IN_PROGRESS'")
    boolean isAnySyncInProgress();

    // Get total records synced for a type
    @Query("SELECT COALESCE(SUM(s.recordsSynced), 0) FROM SyncStatus s WHERE s.syncType = :syncType AND s.state = 'SUCCESS'")
    Long getTotalRecordsSyncedByType(@Param("syncType") String syncType);
}
