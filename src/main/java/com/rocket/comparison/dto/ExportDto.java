package com.rocket.comparison.dto;

import com.rocket.comparison.entity.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for exporting all data from the system (BE-004).
 * Provides a complete snapshot of all entities for data backup/download.
 */
@Data
@Builder
public class ExportDto {

    private ExportMetadata metadata;
    private List<Country> countries;
    private List<Engine> engines;
    private List<LaunchVehicle> launchVehicles;
    private List<SpaceMission> spaceMissions;
    private List<SpaceMilestone> spaceMilestones;
    private List<LaunchSite> launchSites;
    private List<Satellite> satellites;
    private List<CapabilityScore> capabilityScores;

    @Data
    @Builder
    public static class ExportMetadata {
        private LocalDateTime exportedAt;
        private String version;
        private ExportCounts counts;
    }

    @Data
    @Builder
    public static class ExportCounts {
        private long countries;
        private long engines;
        private long launchVehicles;
        private long spaceMissions;
        private long spaceMilestones;
        private long launchSites;
        private long satellites;
        private long capabilityScores;
        private long total;
    }
}
