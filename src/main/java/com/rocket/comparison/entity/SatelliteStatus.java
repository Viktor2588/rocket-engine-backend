package com.rocket.comparison.entity;

/**
 * Operational status of a satellite.
 */
public enum SatelliteStatus {
    PLANNED("Planned", "Satellite is planned but not yet launched"),
    UNDER_CONSTRUCTION("Under Construction", "Satellite is being built"),
    LAUNCHED("Launched", "Recently launched, in commissioning"),
    OPERATIONAL("Operational", "Satellite is fully operational"),
    PARTIALLY_OPERATIONAL("Partially Operational", "Some systems degraded or failed"),
    STANDBY("Standby", "Satellite in standby/reserve mode"),
    DECOMMISSIONED("Decommissioned", "Satellite retired from active service"),
    DECAYED("Decayed", "Satellite has reentered atmosphere"),
    FAILED("Failed", "Satellite failed before or during operations"),
    LOST("Lost", "Contact lost with satellite"),
    UNKNOWN("Unknown", "Status unknown");

    private final String displayName;
    private final String description;

    SatelliteStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == OPERATIONAL || this == PARTIALLY_OPERATIONAL || this == STANDBY;
    }

    public boolean isInSpace() {
        return this == LAUNCHED || this == OPERATIONAL || this == PARTIALLY_OPERATIONAL ||
               this == STANDBY || this == DECOMMISSIONED || this == LOST;
    }

    public boolean isTerminated() {
        return this == DECOMMISSIONED || this == DECAYED || this == FAILED || this == LOST;
    }
}
