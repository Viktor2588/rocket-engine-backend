package com.rocket.comparison.entity;

/**
 * Operational status of a launch site.
 */
public enum LaunchSiteStatus {
    OPERATIONAL("Operational", "Launch site is fully operational"),
    UNDER_CONSTRUCTION("Under Construction", "Launch site is being built"),
    PLANNED("Planned", "Launch site is planned"),
    PARTIAL("Partial", "Some facilities operational, others under construction"),
    MAINTENANCE("Maintenance", "Temporarily closed for maintenance"),
    STANDBY("Standby", "Operational but not actively used"),
    RETIRED("Retired", "Launch site no longer in use"),
    DECOMMISSIONED("Decommissioned", "Launch site permanently closed"),
    DESTROYED("Destroyed", "Launch site destroyed or damaged beyond repair");

    private final String displayName;
    private final String description;

    LaunchSiteStatus(String displayName, String description) {
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
        return this == OPERATIONAL || this == PARTIAL || this == STANDBY;
    }

    public boolean canLaunch() {
        return this == OPERATIONAL || this == PARTIAL;
    }
}
