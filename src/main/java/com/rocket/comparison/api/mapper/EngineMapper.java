package com.rocket.comparison.api.mapper;

import com.rocket.comparison.api.dto.EngineSummaryDto;
import com.rocket.comparison.entity.Engine;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for Engine entity to DTOs (BE-032)
 */
@Component
public class EngineMapper {

    public EngineSummaryDto toSummaryDto(Engine engine) {
        if (engine == null) return null;

        // Calculate thrust in kN for display
        Double thrustKn = engine.getThrustN() != null ? engine.getThrustN() / 1000.0 : null;

        return EngineSummaryDto.builder()
                .id(engine.getId())
                .name(engine.getName())
                .designer(engine.getDesigner())
                .origin(engine.getOrigin())
                .propellant(engine.getPropellant())
                .status(engine.getStatus())
                .ispS(engine.getIsp_s())
                .ispVacuum(engine.getIsp_s())  // Same value for now, vacuum ISP
                .thrustN(engine.getThrustN())
                .thrustKn(thrustKn)
                .thrustToWeightRatio(engine.calculateThrustToWeightRatio())
                .powerCycle(engine.getPowerCycle())
                .massKg(engine.getMassKg())
                .vehicle(engine.getVehicle())
                .use(engine.getUse())
                .build();
    }

    public List<EngineSummaryDto> toSummaryDtoList(List<Engine> engines) {
        return engines.stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public Page<EngineSummaryDto> toSummaryDtoPage(Page<Engine> enginePage) {
        return enginePage.map(this::toSummaryDto);
    }
}
