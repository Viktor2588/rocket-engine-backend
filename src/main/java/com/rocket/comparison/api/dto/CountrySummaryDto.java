package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for country list endpoints (BE-030)
 * Smaller payload than full entity - only essential fields
 */
@Data
@Builder
public class CountrySummaryDto {
    private Long id;
    private String name;
    private String isoCode;
    private String flagUrl;
    private String spaceAgencyAcronym;
    private Double overallCapabilityScore;
    private String region;
    private Boolean independentLaunchCapable;
    private Boolean humanSpaceflightCapable;
}
