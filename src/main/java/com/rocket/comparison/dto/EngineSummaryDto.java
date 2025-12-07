package com.rocket.comparison.dto;

import com.rocket.comparison.entity.Engine;

/**
 * Lightweight DTO for engine summary information.
 */
public record EngineSummaryDto(
    Long id,
    String name,
    String designer,
    String countryName,
    Long thrustN,
    Double ispS,
    String propellant,
    String powerCycle,
    String status,
    Double thrustToWeightRatio
) {
    public static EngineSummaryDto from(Engine engine) {
        return new EngineSummaryDto(
            engine.getId(),
            engine.getName(),
            engine.getDesigner(),
            engine.getCountry() != null ? engine.getCountry().getName() : engine.getOrigin(),
            engine.getThrustN(),
            engine.getIsp_s(),
            engine.getPropellant(),
            engine.getPowerCycle(),
            engine.getStatus(),
            engine.calculateThrustToWeightRatio()
        );
    }
}
