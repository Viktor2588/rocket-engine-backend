package com.rocket.comparison.service;

import com.rocket.comparison.dto.*;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.integration.truthledger.TruthLedgerClient;
import com.rocket.comparison.integration.truthledger.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for managing engine facts from Truth Ledger
 * Provides verified engine specifications with confidence scores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EngineFactService {

    private final TruthLedgerClient truthLedgerClient;
    private final EngineService engineService;

    @Value("${truthledger.enabled:true}")
    private boolean truthLedgerEnabled;

    @Value("${truthledger.default-truth-slider:0.5}")
    private double defaultTruthSlider;

    // Engine field mappings to truth-ledger attribute patterns
    private static final Map<String, String> FIELD_TO_ATTRIBUTE = Map.of(
        "thrustN", "engines.thrust_n",
        "isp_s", "engines.isp_s",
        "massKg", "engines.mass_kg",
        "chamberPressureBar", "engines.chamber_pressure_bar",
        "ofRatio", "engines.of_ratio"
    );

    /**
     * Get all verified facts for an engine
     */
    @Cacheable(value = "engineFacts", key = "#engineId + '-' + #truthSlider", condition = "#root.target.truthLedgerEnabled")
    public EngineFactsDto getEngineFacts(Long engineId, Double truthSlider) {
        double slider = truthSlider != null ? truthSlider : defaultTruthSlider;

        if (!truthLedgerEnabled) {
            log.debug("Truth Ledger disabled, returning local data only for engine {}", engineId);
            return buildLocalOnlyResponse(engineId);
        }

        // First, find the Truth Ledger entity for this engine
        Optional<EntityFactsResponseDto.EntityDto> entityOpt = truthLedgerClient.findEntityByEngineId(engineId);

        if (entityOpt.isEmpty()) {
            log.info("No Truth Ledger entity found for engine {}, returning local data", engineId);
            return buildLocalOnlyResponse(engineId);
        }

        String entityId = entityOpt.get().getId();

        // Get all facts for this entity
        Optional<EntityFactsResponseDto> factsResponse = truthLedgerClient.getEntityFacts(entityId, slider);

        if (factsResponse.isEmpty()) {
            log.warn("Failed to get facts for entity {}, falling back to local data", entityId);
            return buildLocalOnlyResponse(engineId);
        }

        return buildFactsResponse(engineId, entityOpt.get(), factsResponse.get(), slider);
    }

    /**
     * Get a specific verified fact for an engine field
     */
    @Cacheable(value = "engineFieldFact", key = "#engineId + '-' + #fieldName + '-' + #truthSlider")
    public Optional<EngineFieldFactDto> getEngineFieldFact(Long engineId, String fieldName, Double truthSlider) {
        double slider = truthSlider != null ? truthSlider : defaultTruthSlider;

        if (!truthLedgerEnabled) {
            return Optional.empty();
        }

        String attributePattern = FIELD_TO_ATTRIBUTE.get(fieldName);
        if (attributePattern == null) {
            log.warn("Unknown field name: {}", fieldName);
            return Optional.empty();
        }

        Optional<FactResponseDto> factResponse = truthLedgerClient.resolveEngineField(engineId, attributePattern, slider);

        return factResponse.map(fact -> EngineFieldFactDto.builder()
            .engineId(engineId)
            .fieldName(fieldName)
            .attributePattern(attributePattern)
            .value(fact.getBestAnswer() != null ? fact.getBestAnswer().getValue() : null)
            .truthScore(fact.getBestAnswer() != null ? fact.getBestAnswer().getTruthDisplay() : null)
            .status(fact.getStatusDisplay())
            .conflictPresent(fact.getConflictPresent())
            .sourceCount(fact.getBestAnswer() != null ? fact.getBestAnswer().getIndependentSources() : 0)
            .evidence(fact.getBestAnswer() != null ? fact.getBestAnswer().getEvidence() : Collections.emptyList())
            .alternatives(fact.getAlternatives())
            .build());
    }

    /**
     * Get conflicts for an engine
     */
    public List<ConflictGroupDto> getEngineConflicts(Long engineId) {
        if (!truthLedgerEnabled) {
            return Collections.emptyList();
        }

        Optional<EntityFactsResponseDto.EntityDto> entityOpt = truthLedgerClient.findEntityByEngineId(engineId);

        if (entityOpt.isEmpty()) {
            return Collections.emptyList();
        }

        return truthLedgerClient.getConflictGroups(entityOpt.get().getId(), 50);
    }

    /**
     * Get engine with verified facts merged
     * Returns local engine data with truth scores attached to verified fields
     */
    public Optional<EngineWithFactsDto> getEngineWithFacts(Long engineId, Double truthSlider) {
        Optional<Engine> engineOpt = engineService.getEngineById(engineId);

        if (engineOpt.isEmpty()) {
            return Optional.empty();
        }

        Engine engine = engineOpt.get();
        EngineFactsDto facts = getEngineFacts(engineId, truthSlider);

        return Optional.of(EngineWithFactsDto.builder()
            .id(engine.getId())
            .name(engine.getName())
            .origin(engine.getOrigin())
            .designer(engine.getDesigner())
            .vehicle(engine.getVehicle())
            .status(engine.getStatus())
            .use(engine.getUse())
            .propellant(engine.getPropellant())
            .powerCycle(engine.getPowerCycle())
            .description(engine.getDescription())
            // Verified fields with truth scores
            .thrustN(VerifiedField.of(engine.getThrustN(), findFactTruthScore(facts, "thrustN")))
            .isp_s(VerifiedField.of(engine.getIsp_s(), findFactTruthScore(facts, "isp_s")))
            .massKg(VerifiedField.of(engine.getMassKg(), findFactTruthScore(facts, "massKg")))
            .chamberPressureBar(VerifiedField.of(engine.getChamberPressureBar(), findFactTruthScore(facts, "chamberPressureBar")))
            .ofRatio(VerifiedField.of(engine.getOfRatio(), findFactTruthScore(facts, "ofRatio")))
            // Computed field
            .thrustToWeightRatio(engine.calculateThrustToWeightRatio())
            // Verification metadata
            .verificationStatus(facts.getOverallStatus())
            .conflictsPresent(facts.getConflictsPresent())
            .truthLedgerEntityId(facts.getEntityId())
            .build());
    }

    // ==================== Private Helpers ====================

    private EngineFactsDto buildLocalOnlyResponse(Long engineId) {
        Optional<Engine> engineOpt = engineService.getEngineById(engineId);

        return EngineFactsDto.builder()
            .engineId(engineId)
            .engineName(engineOpt.map(Engine::getName).orElse("Unknown"))
            .entityId(null)
            .overallStatus("unverified")
            .conflictsPresent(false)
            .facts(Collections.emptyList())
            .truthSliderUsed(defaultTruthSlider)
            .source("local")
            .build();
    }

    private EngineFactsDto buildFactsResponse(Long engineId, EntityFactsResponseDto.EntityDto entity,
                                               EntityFactsResponseDto factsResponse, double slider) {
        Optional<Engine> engineOpt = engineService.getEngineById(engineId);

        List<EngineFactsDto.FactSummary> factSummaries = new ArrayList<>();
        boolean hasConflicts = false;
        int verifiedCount = 0;

        if (factsResponse.getFacts() != null) {
            for (EntityFactsResponseDto.FactDto fact : factsResponse.getFacts()) {
                factSummaries.add(EngineFactsDto.FactSummary.builder()
                    .fieldName(fact.getFieldName())
                    .value(fact.getBestValue())
                    .truthScore(fact.getTruthDisplay())
                    .status(fact.getStatusDisplay())
                    .conflictPresent(Boolean.TRUE.equals(fact.getConflictPresent()))
                    .sourceCount(fact.getSourceCount())
                    .build());

                if (Boolean.TRUE.equals(fact.getConflictPresent())) {
                    hasConflicts = true;
                }
                if ("verified".equals(fact.getStatusDisplay()) || "supported".equals(fact.getStatusDisplay())) {
                    verifiedCount++;
                }
            }
        }

        String overallStatus = hasConflicts ? "disputed" :
            (verifiedCount > 0 ? "verified" : "insufficient");

        return EngineFactsDto.builder()
            .engineId(engineId)
            .engineName(engineOpt.map(Engine::getName).orElse(entity.getCanonicalName()))
            .entityId(entity.getId())
            .overallStatus(overallStatus)
            .conflictsPresent(hasConflicts)
            .facts(factSummaries)
            .truthSliderUsed(slider)
            .source("truth-ledger")
            .build();
    }

    private Double findFactTruthScore(EngineFactsDto facts, String fieldName) {
        if (facts.getFacts() == null) {
            return null;
        }

        String attributePattern = FIELD_TO_ATTRIBUTE.get(fieldName);
        if (attributePattern == null) {
            return null;
        }

        return facts.getFacts().stream()
            .filter(f -> attributePattern.equals(f.getFieldName()) || fieldName.equals(f.getFieldName()))
            .findFirst()
            .map(EngineFactsDto.FactSummary::getTruthScore)
            .orElse(null);
    }
}
