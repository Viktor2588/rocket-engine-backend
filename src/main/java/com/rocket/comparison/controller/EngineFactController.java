package com.rocket.comparison.controller;

import com.rocket.comparison.dto.*;
import com.rocket.comparison.integration.truthledger.dto.ConflictGroupDto;
import com.rocket.comparison.service.EngineFactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for engine fact verification endpoints
 * Integrates with Truth Ledger for verified engine specifications
 */
@RestController
@RequestMapping("/api/engines")
@RequiredArgsConstructor
@Tag(name = "Engine Facts", description = "Verified engine facts from Truth Ledger")
public class EngineFactController {

    private final EngineFactService engineFactService;

    @Operation(
        summary = "Get verified facts for an engine",
        description = "Returns all verified facts for an engine from Truth Ledger with confidence scores"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Engine not found")
    })
    @GetMapping("/{id}/facts")
    public ResponseEntity<EngineFactsDto> getEngineFacts(
            @Parameter(description = "Engine ID") @PathVariable Long id,
            @Parameter(description = "Truth slider (0.0=conservative, 1.0=assertive)")
            @RequestParam(required = false) Double truthSlider) {

        EngineFactsDto facts = engineFactService.getEngineFacts(id, truthSlider);
        return ResponseEntity.ok(facts);
    }

    @Operation(
        summary = "Get verified fact for a specific engine field",
        description = "Returns the verified value for a specific engine field (e.g., thrustN, isp_s)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fact retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Engine or field not found")
    })
    @GetMapping("/{id}/facts/{fieldName}")
    public ResponseEntity<EngineFieldFactDto> getEngineFieldFact(
            @Parameter(description = "Engine ID") @PathVariable Long id,
            @Parameter(description = "Field name (thrustN, isp_s, massKg, chamberPressureBar, ofRatio)")
            @PathVariable String fieldName,
            @Parameter(description = "Truth slider (0.0=conservative, 1.0=assertive)")
            @RequestParam(required = false) Double truthSlider) {

        Optional<EngineFieldFactDto> fact = engineFactService.getEngineFieldFact(id, fieldName, truthSlider);
        return fact.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get conflicts for an engine",
        description = "Returns all data conflicts for an engine from Truth Ledger"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conflicts retrieved successfully")
    })
    @GetMapping("/{id}/conflicts")
    public ResponseEntity<List<ConflictGroupDto>> getEngineConflicts(
            @Parameter(description = "Engine ID") @PathVariable Long id) {

        List<ConflictGroupDto> conflicts = engineFactService.getEngineConflicts(id);
        return ResponseEntity.ok(conflicts);
    }

    @Operation(
        summary = "Get engine with verified facts",
        description = "Returns engine data with truth scores attached to verified fields"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Engine with facts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Engine not found")
    })
    @GetMapping("/{id}/verified")
    public ResponseEntity<EngineWithFactsDto> getEngineWithFacts(
            @Parameter(description = "Engine ID") @PathVariable Long id,
            @Parameter(description = "Truth slider (0.0=conservative, 1.0=assertive)")
            @RequestParam(required = false) Double truthSlider) {

        Optional<EngineWithFactsDto> engine = engineFactService.getEngineWithFacts(id, truthSlider);
        return engine.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
