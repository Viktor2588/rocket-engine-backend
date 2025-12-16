package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Aggregated DTO for country detail pages (BE-050)
 * Combines country detail info with summaries of related entities
 * Reduces frontend API calls from 6+ to 1
 */
@Data
@Builder
public class CountryDetailsDto {
    private CountryDetailDto country;
    private List<EngineSummaryDto> engines;
    private List<LaunchVehicleSummaryDto> launchVehicles;
    private List<MissionSummaryDto> missions;
    private List<MilestoneSummaryDto> milestones;
}
