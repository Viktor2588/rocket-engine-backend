package com.rocket.comparison.api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

/**
 * Summary DTO for milestone list endpoints (BE-050)
 */
@Data
@Builder
public class MilestoneSummaryDto {
    private Long id;
    private String title;
    private String milestoneType;
    private LocalDate dateAchieved;
    private String achievedBy;
    private String missionName;
    private Boolean isGlobalFirst;
    private Integer globalRank;
}
