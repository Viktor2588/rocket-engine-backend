package com.rocket.comparison.integration.truthledger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Response from Truth Ledger entity facts API
 * Maps to: GET /api/v1/entities/:entityId/facts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFactsResponseDto {

    private EntityDto entity;
    private List<FactDto> facts;
    private PaginationDto pagination;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EntityDto {
        private String id;
        private String entityType;  // 'engine', 'launch_vehicle', 'country'
        private String canonicalName;
        private Long engineId;
        private Long launchVehicleId;
        private Long countryId;
        private List<String> aliases;
        private Map<String, Object> metadata;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FactDto {
        private String fieldName;
        private String attributePattern;
        private Object bestValue;
        private Double truthDisplay;
        private String statusDisplay;
        private Boolean conflictPresent;
        private Integer sourceCount;
        private List<FactResponseDto.EvidenceDto> evidence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaginationDto {
        private Integer total;
        private Integer limit;
        private Integer offset;
        private Boolean hasMore;
    }
}
