package com.rocket.comparison.dto;

import com.rocket.comparison.entity.Country;

/**
 * Lightweight DTO for country summary information.
 */
public record CountrySummaryDto(
    Long id,
    String name,
    String isoCode,
    String flagUrl,
    String spaceAgencyName,
    Double overallCapabilityScore,
    Integer totalLaunches,
    Double launchSuccessRate
) {
    public static CountrySummaryDto from(Country country) {
        return new CountrySummaryDto(
            country.getId(),
            country.getName(),
            country.getIsoCode(),
            country.getFlagUrl(),
            country.getSpaceAgencyName(),
            country.getOverallCapabilityScore(),
            country.getTotalLaunches(),
            country.getLaunchSuccessRate()
        );
    }
}
