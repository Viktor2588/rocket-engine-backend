package com.rocket.comparison.api.mapper;

import com.rocket.comparison.api.dto.*;
import com.rocket.comparison.entity.*;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Country and related entities (Step 3.2)
 * Generates implementation at compile-time for high performance
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapStructMapper {

    // ==================== Country Mapping ====================

    CountrySummaryDto toSummaryDto(Country country);

    List<CountrySummaryDto> toSummaryDtoList(List<Country> countries);

    CountryDetailDto toDetailDto(Country country);

    default org.springframework.data.domain.Page<CountrySummaryDto> toSummaryDtoPage(
            org.springframework.data.domain.Page<Country> countryPage) {
        return countryPage.map(this::toSummaryDto);
    }

    // ==================== Launch Vehicle Mapping ====================

    LaunchVehicleSummaryDto toLaunchVehicleSummaryDto(LaunchVehicle launchVehicle);

    List<LaunchVehicleSummaryDto> toLaunchVehicleSummaryDtoList(List<LaunchVehicle> launchVehicles);

    // ==================== Mission Mapping ====================

    @Mapping(target = "missionType", expression = "java(mission.getMissionType() != null ? mission.getMissionType().name() : null)")
    @Mapping(target = "status", expression = "java(mission.getStatus() != null ? mission.getStatus().name() : null)")
    @Mapping(target = "destination", expression = "java(mission.getDestination() != null ? mission.getDestination().getDisplayName() : null)")
    MissionSummaryDto toMissionSummaryDto(SpaceMission mission);

    List<MissionSummaryDto> toMissionSummaryDtoList(List<SpaceMission> missions);

    // ==================== Milestone Mapping ====================

    @Mapping(target = "milestoneType", expression = "java(milestone.getMilestoneType() != null ? milestone.getMilestoneType().name() : null)")
    MilestoneSummaryDto toMilestoneSummaryDto(SpaceMilestone milestone);

    List<MilestoneSummaryDto> toMilestoneSummaryDtoList(List<SpaceMilestone> milestones);

    // ==================== Complex Mapping Methods ====================

    /**
     * Creates CountryDetailDto with entity counts
     * This is a default method because it requires additional parameters beyond the source
     */
    default CountryDetailDto toDetailDtoWithCounts(Country country,
                                                    int engineCount,
                                                    int launchVehicleCount,
                                                    int missionCount,
                                                    int milestoneCount) {
        CountryDetailDto dto = toDetailDto(country);
        if (dto != null) {
            dto.setEngineCount(engineCount);
            dto.setLaunchVehicleCount(launchVehicleCount);
            dto.setMissionCount(missionCount);
            dto.setMilestoneCount(milestoneCount);
        }
        return dto;
    }
}
