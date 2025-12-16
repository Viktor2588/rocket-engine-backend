package com.rocket.comparison.api.mapper;

import com.rocket.comparison.api.dto.EngineSummaryDto;
import com.rocket.comparison.entity.Engine;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Engine entity (Step 3.2)
 * Generates implementation at compile-time for high performance
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EngineMapStructMapper {

    @Mapping(target = "ispS", source = "isp_s")
    @Mapping(target = "thrustToWeightRatio", expression = "java(engine.calculateThrustToWeightRatio())")
    EngineSummaryDto toSummaryDto(Engine engine);

    List<EngineSummaryDto> toSummaryDtoList(List<Engine> engines);

    /**
     * Maps Page<Engine> to Page<EngineSummaryDto>
     * Note: This requires a default method because MapStruct doesn't directly support Page
     */
    default org.springframework.data.domain.Page<EngineSummaryDto> toSummaryDtoPage(
            org.springframework.data.domain.Page<Engine> enginePage) {
        return enginePage.map(this::toSummaryDto);
    }
}
