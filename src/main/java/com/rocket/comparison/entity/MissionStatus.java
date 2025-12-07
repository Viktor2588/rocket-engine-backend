package com.rocket.comparison.entity;

/**
 * Status of a space mission.
 */
public enum MissionStatus {
    PLANNED("Planned", "Mission is planned but not yet launched"),
    IN_DEVELOPMENT("In Development", "Mission is in active development"),
    LAUNCHED("Launched", "Mission has launched and is in transit"),
    ACTIVE("Active", "Mission is currently operational"),
    COMPLETED("Completed", "Mission successfully completed all objectives"),
    PARTIAL_SUCCESS("Partial Success", "Mission achieved some but not all objectives"),
    FAILED("Failed", "Mission failed to achieve primary objectives"),
    LOST("Lost", "Contact lost with spacecraft"),
    RETIRED("Retired", "Mission concluded after extended operation"),
    CANCELLED("Cancelled", "Mission cancelled before launch");

    private final String displayName;
    private final String description;

    MissionStatus(String displayName, String description) {
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
        return this == LAUNCHED || this == ACTIVE;
    }

    public boolean isCompleted() {
        return this == COMPLETED || this == PARTIAL_SUCCESS || this == RETIRED;
    }

    public boolean isFailed() {
        return this == FAILED || this == LOST;
    }

    public boolean isPending() {
        return this == PLANNED || this == IN_DEVELOPMENT;
    }
}
