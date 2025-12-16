-- V3__add_sync_status_table.sql
-- BE-061: Create sync_status table for persisting sync state across restarts

CREATE TABLE sync_status (
    id BIGSERIAL PRIMARY KEY,
    sync_type VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    records_synced INTEGER,
    error_message TEXT,
    source_api VARCHAR(100)
);

-- Index for quick lookups by sync type
CREATE INDEX idx_sync_status_type ON sync_status (sync_type);

-- Index for finding latest syncs
CREATE INDEX idx_sync_status_started_at ON sync_status (started_at DESC);

-- Index for finding in-progress syncs
CREATE INDEX idx_sync_status_state ON sync_status (state);

-- Composite index for type + state queries
CREATE INDEX idx_sync_status_type_state ON sync_status (sync_type, state);

-- Add comment
COMMENT ON TABLE sync_status IS 'Tracks API sync operations for persistence across restarts';
