package com.rocket.comparison.integration.truthledger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Response from Truth Ledger entity list API
 * Maps to: GET /api/v1/entities
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityListResponseDto {

    private List<TruthLedgerEntityDto> entities;
    private Integer count;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TruthLedgerEntityDto {
        private String id;
        private String entityType;  // 'engine', 'launch_vehicle', 'country', 'launch_site', 'space_mission'
        private String canonicalName;
        private Long engineId;          // Backend ID if linked
        private Long launchVehicleId;   // Backend ID if linked
        private Long countryId;         // Backend ID if linked
        private Long launchSiteId;      // Backend ID if linked
        private Long spaceMissionId;    // Backend ID if linked
        private List<String> aliases;
        private Map<String, Object> metadata;
    }
}
