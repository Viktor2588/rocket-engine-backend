package com.rocket.comparison.integration.truthledger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Conflict group from Truth Ledger
 * Maps to: GET /api/v1/conflict-groups/:id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConflictGroupDto {

    private String id;
    private String claimKeyHash;
    private String entityId;
    private String attributeId;
    private Map<String, Object> scopeJson;
    private Boolean conflictPresent;
    private String statusFactual;  // 'disputed', 'verified', 'insufficient'
    private Integer claimCount;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    // Expanded details when fetching single conflict
    private EntityFactsResponseDto.EntityDto entity;
    private AttributeDto attribute;
    private List<ClaimDto> claims;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AttributeDto {
        private String id;
        private String pattern;
        private String displayName;
        private String unit;
        private String valueType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClaimDto {
        private String id;
        private Object valueRaw;
        private Object valueNormalized;
        private String unit;
        private Double truthRaw;
        private Double truthDisplay;
        private Integer supportCount;
        private Integer contradictCount;
        private List<FactResponseDto.EvidenceDto> evidence;
    }
}
