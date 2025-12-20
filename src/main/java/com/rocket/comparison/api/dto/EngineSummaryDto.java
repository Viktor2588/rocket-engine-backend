package com.rocket.comparison.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for engine list endpoints (BE-032)
 * Field names match frontend expectations for proper display
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

    // ISP fields - use common naming conventions
    @JsonProperty("isp")
    private Double ispS;

    @JsonProperty("ispVacuum")
    private Double ispVacuum;

    // Thrust fields
    @JsonProperty("thrust")
    private Long thrustN;

    @JsonProperty("thrustKn")
    private Double thrustKn;

    // Performance ratios
    @JsonProperty("twRatio")
    private Double thrustToWeightRatio;

    // Additional fields frontend may need
    private String powerCycle;
    private Double massKg;
    private String vehicle;
    private String use;
}
