package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Detail DTO for single country endpoints (BE-030)
 * Complete country information without JPA relationships
 */
@Data
@Builder
public class CountryDetailDto {
    private Long id;
    private String name;
    private String isoCode;
    private String flagUrl;
    private String region;
    
    // Space Agency Info
    private String spaceAgencyName;
    private String spaceAgencyAcronym;
    private Integer spaceAgencyFounded;
    
    // Budget & Resources
    private BigDecimal annualBudgetUsd;
    private Double budgetAsPercentOfGdp;
    private Integer totalSpaceAgencyEmployees;
    private Integer activeAstronauts;
    
    // Launch Statistics
    private Integer totalLaunches;
    private Integer successfulLaunches;
    private Double launchSuccessRate;
    
    // Capabilities
    private Boolean humanSpaceflightCapable;
    private Boolean independentLaunchCapable;
    private Boolean reusableRocketCapable;
    private Boolean deepSpaceCapable;
    private Boolean spaceStationCapable;
    private Boolean lunarLandingCapable;
    private Boolean marsLandingCapable;
    
    // Scores
    private Double overallCapabilityScore;
    
    // Description
    private String description;
    
    // Counts (aggregated)
    private Integer engineCount;
    private Integer launchVehicleCount;
    private Integer missionCount;
    private Integer milestoneCount;
}
