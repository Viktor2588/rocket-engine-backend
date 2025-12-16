package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for launch vehicle list endpoints (BE-050)
 */
@Data
@Builder
public class LaunchVehicleSummaryDto {
    private Long id;
    private String name;
    private String family;
    private String manufacturer;
    private String status;
    private Integer payloadToLeoKg;
    private Integer payloadToGtoKg;
    private Boolean reusable;
    private Boolean humanRated;
    private Boolean active;
}
