package com.rocket.comparison.api.mapper;

import com.rocket.comparison.api.dto.CountryDetailDto;
import com.rocket.comparison.api.dto.CountrySummaryDto;
import com.rocket.comparison.entity.Country;
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
}
