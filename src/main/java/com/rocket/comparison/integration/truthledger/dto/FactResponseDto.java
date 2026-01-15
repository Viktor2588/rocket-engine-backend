package com.rocket.comparison.integration.truthledger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Response from Truth Ledger fact resolution API
 * Maps to: GET /api/v1/facts/:claimKeyHash or /api/v1/entities/:entityId/facts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactResponseDto {

    private String claimKey;
    private Double sliderUsed;
    private String modeLabel;  // 'Conservative' | 'Balanced' | 'Assertive'
    private BestAnswerDto bestAnswer;
    private String statusDisplay;  // 'verified' | 'supported' | 'disputed' | 'insufficient'
    private Boolean conflictPresent;
    private List<AlternativeDto> alternatives;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BestAnswerDto {
        private Object value;
        private Double truthDisplay;
        private Integer independentSources;
        private Double supportScore;
        private Double contradictionScore;
        private List<EvidenceDto> evidence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EvidenceDto {
        private String sourceName;
        private String documentTitle;
        private String stance;  // 'support' | 'contradict' | 'neutral'
        private Double extractionConfidence;
        private String snippetText;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AlternativeDto {
        private Object value;
        private Double truthDisplay;
        private Integer supportingSourceCount;
    }
}
