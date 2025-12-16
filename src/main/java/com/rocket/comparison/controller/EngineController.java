package com.rocket.comparison.controller;

import com.rocket.comparison.api.dto.EngineSummaryDto;
import com.rocket.comparison.api.mapper.EngineMapper;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.service.EngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/engines")
@RequiredArgsConstructor
public class EngineController {

    private final EngineService engineService;
    private final EngineMapper engineMapper;

    @GetMapping
    public ResponseEntity<?> getAllEngines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean unpaged) {
        // If unpaged=true, return simple list (for frontend compatibility)
        if (Boolean.TRUE.equals(unpaged)) {
            return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getAllEngines()));
        }
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);
        return ResponseEntity.ok(engineMapper.toSummaryDtoPage(engineService.getAllEngines(pageable)));
    }

    @GetMapping({"/all", "/list"})
    public ResponseEntity<List<EngineSummaryDto>> getAllEnginesUnpaged() {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getAllEngines()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Engine> getEngineById(@PathVariable Long id) {
        Optional<Engine> engine = engineService.getEngineById(id);
        return engine.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Engine> createEngine(@Valid @RequestBody Engine engine) {
        Engine savedEngine = engineService.saveEngine(engine);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEngine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Engine> updateEngine(@PathVariable Long id, @Valid @RequestBody Engine engineDetails) {
        try {
            Engine updatedEngine = engineService.updateEngine(id, engineDetails);
            return ResponseEntity.ok(updatedEngine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngine(@PathVariable Long id) {
        if (engineService.getEngineById(id).isPresent()) {
            engineService.deleteEngine(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/designer/{designer}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByDesigner(@PathVariable String designer) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByDesigner(designer)));
    }

    @GetMapping("/propellant/{propellant}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByPropellant(@PathVariable String propellant) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByPropellant(propellant)));
    }

    @GetMapping("/thrust-min/{thrust}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByMinThrust(@PathVariable Long thrust) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByMinThrust(thrust)));
    }

    @GetMapping("/isp-min/{isp}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByMinIsp(@PathVariable Double isp) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByMinIsp(isp)));
    }

    // Country-based endpoints
    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByCountryId(@PathVariable Long countryId) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByCountryId(countryId)));
    }

    @GetMapping("/by-country-code/{isoCode}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByCountryCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByCountryCode(isoCode)));
    }

    @GetMapping("/by-origin/{origin}")
    public ResponseEntity<List<EngineSummaryDto>> getEnginesByOrigin(@PathVariable String origin) {
        return ResponseEntity.ok(engineMapper.toSummaryDtoList(engineService.getEnginesByOrigin(origin)));
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compareEngines(@RequestParam Long engine1Id, @RequestParam Long engine2Id) {
        Optional<Engine> engine1 = engineService.getEngineById(engine1Id);
        Optional<Engine> engine2 = engineService.getEngineById(engine2Id);

        if (engine1.isEmpty() || engine2.isEmpty()) {
            return ResponseEntity.badRequest().body("One or both engines not found");
        }

        return ResponseEntity.ok(new ComparisonResult(engine1.get(), engine2.get()));
    }

    @GetMapping("/compare/by-twr")
    public ResponseEntity<?> compareEnginesByThrustToWeightRatio(@RequestParam Long engine1Id, @RequestParam Long engine2Id) {
        Optional<Engine> engine1 = engineService.getEngineById(engine1Id);
        Optional<Engine> engine2 = engineService.getEngineById(engine2Id);

        if (engine1.isEmpty() || engine2.isEmpty()) {
            return ResponseEntity.badRequest().body("One or both engines not found");
        }

        return ResponseEntity.ok(new ThrustToWeightComparisonDTO(engine1.get(), engine2.get()));
    }

    // Inner class for comparison result
    public record ComparisonResult(Engine engine1, Engine engine2) {}

    // Inner class for thrust-to-weight ratio comparison DTO
    public record ThrustToWeightComparisonDTO(
            Long engine1_id,
            String engine1_name,
            Double engine1_thrustToWeightRatio,
            Long engine2_id,
            String engine2_name,
            Double engine2_thrustToWeightRatio,
            String betterPerformer,
            Double ratioPercentageDifference
    ) {
        public ThrustToWeightComparisonDTO(Engine engine1, Engine engine2) {
            this(
                    engine1.getId(),
                    engine1.getName(),
                    engine1.calculateThrustToWeightRatio(),
                    engine2.getId(),
                    engine2.getName(),
                    engine2.calculateThrustToWeightRatio(),
                    calculateBetterPerformer(engine1.calculateThrustToWeightRatio(), engine2.calculateThrustToWeightRatio()),
                    calculatePercentageDifference(engine1.calculateThrustToWeightRatio(), engine2.calculateThrustToWeightRatio())
            );
        }

        private static String calculateBetterPerformer(Double twr1, Double twr2) {
            if (twr1 == null && twr2 == null) return "Cannot compare - both engines have unknown mass";
            if (twr1 == null) return "Engine 2";
            if (twr2 == null) return "Engine 1";
            return twr1 > twr2 ? "Engine 1" : "Engine 2";
        }

        private static Double calculatePercentageDifference(Double twr1, Double twr2) {
            if (twr1 == null || twr2 == null) return null;
            double higher = Math.max(twr1, twr2);
            double lower = Math.min(twr1, twr2);
            return ((higher - lower) / lower) * 100.0;
        }
    }
}
