package com.rocket.comparison.dto;

import com.rocket.comparison.integration.truthledger.dto.FactResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for a single engine field fact from Truth Ledger
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineFieldFactDto {

    private Long engineId;
    private String fieldName;
    private String attributePattern;
    private Object value;
    private Double truthScore;
    private String status;
    private Boolean conflictPresent;
    private Integer sourceCount;
    private List<FactResponseDto.EvidenceDto> evidence;
    private List<FactResponseDto.AlternativeDto> alternatives;
}
