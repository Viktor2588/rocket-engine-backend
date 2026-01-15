package com.rocket.comparison.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for engine facts from Truth Ledger
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineFactsDto {

    private Long engineId;
    private String engineName;
    private String entityId;  // Truth Ledger entity UUID
    private String overallStatus;  // 'verified', 'disputed', 'insufficient', 'unverified'
    private Boolean conflictsPresent;
    private List<FactSummary> facts;
    private Double truthSliderUsed;
    private String source;  // 'truth-ledger' or 'local'

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FactSummary {
        private String fieldName;
        private Object value;
        private Double truthScore;
        private String status;
        private Boolean conflictPresent;
        private Integer sourceCount;
    }
}
