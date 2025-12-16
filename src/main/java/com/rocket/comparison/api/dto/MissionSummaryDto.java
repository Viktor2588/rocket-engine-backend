package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

/**
 * Summary DTO for mission list endpoints (BE-050)
 */
@Data
@Builder
public class MissionSummaryDto {
    private Long id;
    private String name;
    private String missionType;
    private String status;
    private LocalDate launchDate;
    private String destination;
    private String operator;
    private String launchVehicleName;
    private Boolean crewed;
}
