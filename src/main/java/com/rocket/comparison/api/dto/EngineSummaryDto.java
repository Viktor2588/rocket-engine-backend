package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for engine list endpoints (BE-032)
 */
@Data
@Builder
public class EngineSummaryDto {
    private Long id;
    private String name;
    private String designer;
    private String origin;
    private String propellant;
    private String status;
    private Double ispS;
    private Long thrustN;
    private Double thrustToWeightRatio;
}
