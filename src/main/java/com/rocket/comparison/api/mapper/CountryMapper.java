package com.rocket.comparison.api.mapper;

import com.rocket.comparison.api.dto.*;
import com.rocket.comparison.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for Country entity to DTOs (BE-030)
 * Manual mapping - can be replaced with MapStruct if needed
 */
@Component
public class CountryMapper {

    public CountrySummaryDto toSummaryDto(Country country) {
        if (country == null) return null;
        
        return CountrySummaryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .isoCode(country.getIsoCode())
                .flagUrl(country.getFlagUrl())
                .spaceAgencyAcronym(country.getSpaceAgencyAcronym())
                .overallCapabilityScore(country.getOverallCapabilityScore())
                .region(country.getRegion())
                .independentLaunchCapable(country.getIndependentLaunchCapable())
                .humanSpaceflightCapable(country.getHumanSpaceflightCapable())
                .build();
    }

    public List<CountrySummaryDto> toSummaryDtoList(List<Country> countries) {
        return countries.stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public Page<CountrySummaryDto> toSummaryDtoPage(Page<Country> countryPage) {
        return countryPage.map(this::toSummaryDto);
    }

    public CountryDetailDto toDetailDto(Country country) {
        if (country == null) return null;
        
        return CountryDetailDto.builder()
                .id(country.getId())
                .name(country.getName())
                .isoCode(country.getIsoCode())
                .flagUrl(country.getFlagUrl())
                .region(country.getRegion())
                .spaceAgencyName(country.getSpaceAgencyName())
                .spaceAgencyAcronym(country.getSpaceAgencyAcronym())
                .spaceAgencyFounded(country.getSpaceAgencyFounded())
                .annualBudgetUsd(country.getAnnualBudgetUsd())
                .budgetAsPercentOfGdp(country.getBudgetAsPercentOfGdp())
                .totalSpaceAgencyEmployees(country.getTotalSpaceAgencyEmployees())
                .activeAstronauts(country.getActiveAstronauts())
                .totalLaunches(country.getTotalLaunches())
                .successfulLaunches(country.getSuccessfulLaunches())
                .launchSuccessRate(country.getLaunchSuccessRate())
                .humanSpaceflightCapable(country.getHumanSpaceflightCapable())
                .independentLaunchCapable(country.getIndependentLaunchCapable())
                .reusableRocketCapable(country.getReusableRocketCapable())
                .deepSpaceCapable(country.getDeepSpaceCapable())
                .spaceStationCapable(country.getSpaceStationCapable())
                .lunarLandingCapable(country.getLunarLandingCapable())
                .marsLandingCapable(country.getMarsLandingCapable())
                .overallCapabilityScore(country.getOverallCapabilityScore())
                .description(country.getDescription())
                .build();
    }

    public CountryDetailDto toDetailDtoWithCounts(Country country, int engineCount, int launchVehicleCount,
                                                   int missionCount, int milestoneCount) {
        CountryDetailDto dto = toDetailDto(country);
        if (dto != null) {
            dto.setEngineCount(engineCount);
            dto.setLaunchVehicleCount(launchVehicleCount);
            dto.setMissionCount(missionCount);
            dto.setMilestoneCount(milestoneCount);
        }
        return dto;
    }

    // ==================== Launch Vehicle Mapping ====================

    public LaunchVehicleSummaryDto toLaunchVehicleSummaryDto(LaunchVehicle lv) {
        if (lv == null) return null;

        return LaunchVehicleSummaryDto.builder()
                .id(lv.getId())
                .name(lv.getName())
                .family(lv.getFamily())
                .manufacturer(lv.getManufacturer())
                .status(lv.getStatus())
                .payloadToLeoKg(lv.getPayloadToLeoKg())
                .payloadToGtoKg(lv.getPayloadToGtoKg())
                .reusable(lv.getReusable())
                .humanRated(lv.getHumanRated())
                .active(lv.getActive())
                .build();
    }

    public List<LaunchVehicleSummaryDto> toLaunchVehicleSummaryDtoList(List<LaunchVehicle> launchVehicles) {
        return launchVehicles.stream()
                .map(this::toLaunchVehicleSummaryDto)
                .collect(Collectors.toList());
    }

    // ==================== Mission Mapping ====================

    public MissionSummaryDto toMissionSummaryDto(SpaceMission mission) {
        if (mission == null) return null;

        return MissionSummaryDto.builder()
                .id(mission.getId())
                .name(mission.getName())
                .missionType(mission.getMissionType() != null ? mission.getMissionType().name() : null)
                .status(mission.getStatus() != null ? mission.getStatus().name() : null)
                .launchDate(mission.getLaunchDate())
                .destination(mission.getDestination() != null ? mission.getDestination().getDisplayName() : null)
                .operator(mission.getOperator())
                .launchVehicleName(mission.getLaunchVehicleName())
                .crewed(mission.getCrewed())
                .build();
    }

    public List<MissionSummaryDto> toMissionSummaryDtoList(List<SpaceMission> missions) {
        return missions.stream()
                .map(this::toMissionSummaryDto)
                .collect(Collectors.toList());
    }

    // ==================== Milestone Mapping ====================

    public MilestoneSummaryDto toMilestoneSummaryDto(SpaceMilestone milestone) {
        if (milestone == null) return null;

        return MilestoneSummaryDto.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .milestoneType(milestone.getMilestoneType() != null ? milestone.getMilestoneType().name() : null)
                .dateAchieved(milestone.getDateAchieved())
                .achievedBy(milestone.getAchievedBy())
                .missionName(milestone.getMissionName())
                .isGlobalFirst(milestone.getIsGlobalFirst())
                .globalRank(milestone.getGlobalRank())
                .build();
    }

    public List<MilestoneSummaryDto> toMilestoneSummaryDtoList(List<SpaceMilestone> milestones) {
        return milestones.stream()
                .map(this::toMilestoneSummaryDto)
                .collect(Collectors.toList());
    }

    // ==================== Aggregated Country Details Mapping ====================

    /**
     * Creates a comprehensive CountryDetailsDto (BE-050)
     * Aggregates country detail with all related entity summaries
     */
    public CountryDetailsDto toCountryDetailsDto(Country country,
                                                  List<Engine> engines,
                                                  List<LaunchVehicle> launchVehicles,
                                                  List<SpaceMission> missions,
                                                  List<SpaceMilestone> milestones,
                                                  EngineMapper engineMapper) {
        return CountryDetailsDto.builder()
                .country(toDetailDtoWithCounts(country,
                        engines.size(),
                        launchVehicles.size(),
                        missions.size(),
                        milestones.size()))
                .engines(engineMapper.toSummaryDtoList(engines))
                .launchVehicles(toLaunchVehicleSummaryDtoList(launchVehicles))
                .missions(toMissionSummaryDtoList(missions))
                .milestones(toMilestoneSummaryDtoList(milestones))
                .build();
    }
}
