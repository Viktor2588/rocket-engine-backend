package com.rocket.comparison.integration.truthledger;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.integration.truthledger.dto.EntityFactsResponseDto;
import com.rocket.comparison.integration.truthledger.dto.EntityListResponseDto;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.EngineRepository;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service for syncing entities from Truth Ledger to the backend database.
 *
 * The Truth Ledger is the source of truth for verified entity data.
 * This service fetches entities and their verified facts from Truth Ledger
 * and creates/updates corresponding entities in the backend.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TruthLedgerSyncService {

    private final TruthLedgerClient truthLedgerClient;
    private final EngineRepository engineRepository;
    private final LaunchVehicleRepository launchVehicleRepository;
    private final CountryRepository countryRepository;

    @Value("${truthledger.enabled:true}")
    private boolean enabled;

    @Value("${truthledger.sync.truth-threshold:0.5}")
    private double truthThreshold;

    /**
     * Sync all engines from Truth Ledger
     * @return Map with sync results (created, updated, errors)
     */
    @Transactional
    public Map<String, Object> syncEngines() {
        if (!enabled) {
            log.warn("Truth Ledger sync is disabled");
            return Map.of("status", "disabled", "message", "Truth Ledger sync is disabled");
        }

        log.info("Starting engine sync from Truth Ledger");
        int created = 0;
        int updated = 0;
        int errors = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            // Fetch all engine entities from Truth Ledger
            List<EntityListResponseDto.TruthLedgerEntityDto> entities =
                truthLedgerClient.listAllEntitiesByType("engine");

            log.info("Found {} engine entities in Truth Ledger", entities.size());

            for (EntityListResponseDto.TruthLedgerEntityDto entity : entities) {
                try {
                    boolean isNew = syncEngine(entity);
                    if (isNew) {
                        created++;
                    } else {
                        updated++;
                    }
                } catch (Exception e) {
                    errors++;
                    String msg = String.format("Failed to sync engine '%s': %s",
                        entity.getCanonicalName(), e.getMessage());
                    errorMessages.add(msg);
                    log.warn(msg, e);
                }
            }

        } catch (Exception e) {
            log.error("Failed to fetch entities from Truth Ledger", e);
            return Map.of(
                "status", "error",
                "message", "Failed to connect to Truth Ledger: " + e.getMessage()
            );
        }

        log.info("Engine sync completed: {} created, {} updated, {} errors", created, updated, errors);

        Map<String, Object> result = new HashMap<>();
        result.put("status", errors == 0 ? "success" : "partial");
        result.put("created", created);
        result.put("updated", updated);
        result.put("errors", errors);
        result.put("total", created + updated);
        if (!errorMessages.isEmpty()) {
            result.put("errorMessages", errorMessages.subList(0, Math.min(10, errorMessages.size())));
        }
        return result;
    }

    /**
     * Sync a single engine from Truth Ledger entity
     * @return true if created, false if updated
     */
    private boolean syncEngine(EntityListResponseDto.TruthLedgerEntityDto entity) {
        String name = entity.getCanonicalName();

        // Check if engine already exists by name
        Optional<Engine> existing = engineRepository.findAll().stream()
            .filter(e -> e.getName().equalsIgnoreCase(name))
            .findFirst();

        Engine engine;
        boolean isNew = existing.isEmpty();

        if (isNew) {
            engine = new Engine();
            engine.setName(name);
            engine.setPropellant("Unknown"); // Required field, will be updated from facts
        } else {
            engine = existing.get();
        }

        // Try to get facts for this entity
        if (entity.getId() != null) {
            try {
                Optional<EntityFactsResponseDto> factsResponse =
                    truthLedgerClient.getEntityFacts(entity.getId(), truthThreshold);

                if (factsResponse.isPresent()) {
                    applyEngineFacts(engine, factsResponse.get().getFacts());
                }
            } catch (Exception e) {
                log.debug("Could not fetch facts for engine {}: {}", name, e.getMessage());
            }
        }

        // Apply metadata if available
        if (entity.getMetadata() != null) {
            applyEngineMetadata(engine, entity.getMetadata());
        }

        engineRepository.save(engine);
        log.debug("{} engine: {}", isNew ? "Created" : "Updated", name);

        return isNew;
    }

    /**
     * Apply verified facts to an engine entity
     */
    private void applyEngineFacts(Engine engine, List<EntityFactsResponseDto.FactDto> facts) {
        if (facts == null) return;

        for (EntityFactsResponseDto.FactDto fact : facts) {
            String field = fact.getAttributePattern();
            Object value = fact.getBestValue();

            if (value == null) continue;

            try {
                switch (field) {
                    case "engines.isp_s", "isp_s", "isp" -> {
                        engine.setIsp_s(toDouble(value));
                    }
                    case "engines.thrust_n", "thrust_n", "thrust" -> {
                        engine.setThrustN(toLong(value));
                    }
                    case "engines.chamber_pressure_bar", "chamber_pressure_bar", "chamber_pressure" -> {
                        engine.setChamberPressureBar(toDouble(value));
                    }
                    case "engines.mass_kg", "mass_kg", "mass" -> {
                        engine.setMassKg(toDouble(value));
                    }
                    case "engines.of_ratio", "of_ratio" -> {
                        engine.setOfRatio(toDouble(value));
                    }
                    case "engines.propellant", "propellant" -> {
                        engine.setPropellant(value.toString());
                    }
                    case "engines.power_cycle", "power_cycle" -> {
                        engine.setPowerCycle(value.toString());
                    }
                    case "engines.designer", "designer", "manufacturer" -> {
                        engine.setDesigner(value.toString());
                    }
                    case "engines.origin", "origin", "country" -> {
                        engine.setOrigin(value.toString());
                        // Try to link to Country entity
                        linkEngineToCountry(engine, value.toString());
                    }
                    case "engines.status", "status" -> {
                        engine.setStatus(value.toString());
                    }
                    case "engines.vehicle", "vehicle" -> {
                        engine.setVehicle(value.toString());
                    }
                    case "engines.use", "use", "stage" -> {
                        engine.setUse(value.toString());
                    }
                    case "engines.family", "family" -> {
                        engine.setFamily(value.toString());
                    }
                    case "engines.description", "description" -> {
                        engine.setDescription(value.toString());
                    }
                }
            } catch (Exception e) {
                log.debug("Could not apply fact {} = {} to engine: {}", field, value, e.getMessage());
            }
        }
    }

    /**
     * Apply metadata from Truth Ledger entity to engine
     */
    private void applyEngineMetadata(Engine engine, Map<String, Object> metadata) {
        if (metadata.containsKey("propellant")) {
            engine.setPropellant(metadata.get("propellant").toString());
        }
        if (metadata.containsKey("designer")) {
            engine.setDesigner(metadata.get("designer").toString());
        }
        if (metadata.containsKey("origin")) {
            engine.setOrigin(metadata.get("origin").toString());
        }
        if (metadata.containsKey("status")) {
            engine.setStatus(metadata.get("status").toString());
        }
        if (metadata.containsKey("powerCycle")) {
            engine.setPowerCycle(metadata.get("powerCycle").toString());
        }
    }

    /**
     * Sync all launch vehicles from Truth Ledger
     */
    @Transactional
    public Map<String, Object> syncLaunchVehicles() {
        if (!enabled) {
            log.warn("Truth Ledger sync is disabled");
            return Map.of("status", "disabled", "message", "Truth Ledger sync is disabled");
        }

        log.info("Starting launch vehicle sync from Truth Ledger");
        int created = 0;
        int updated = 0;
        int errors = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            List<EntityListResponseDto.TruthLedgerEntityDto> entities =
                truthLedgerClient.listAllEntitiesByType("launch_vehicle");

            log.info("Found {} launch vehicle entities in Truth Ledger", entities.size());

            for (EntityListResponseDto.TruthLedgerEntityDto entity : entities) {
                try {
                    boolean isNew = syncLaunchVehicle(entity);
                    if (isNew) {
                        created++;
                    } else {
                        updated++;
                    }
                } catch (Exception e) {
                    errors++;
                    String msg = String.format("Failed to sync launch vehicle '%s': %s",
                        entity.getCanonicalName(), e.getMessage());
                    errorMessages.add(msg);
                    log.warn(msg, e);
                }
            }

        } catch (Exception e) {
            log.error("Failed to fetch entities from Truth Ledger", e);
            return Map.of(
                "status", "error",
                "message", "Failed to connect to Truth Ledger: " + e.getMessage()
            );
        }

        log.info("Launch vehicle sync completed: {} created, {} updated, {} errors", created, updated, errors);

        Map<String, Object> result = new HashMap<>();
        result.put("status", errors == 0 ? "success" : "partial");
        result.put("created", created);
        result.put("updated", updated);
        result.put("errors", errors);
        result.put("total", created + updated);
        if (!errorMessages.isEmpty()) {
            result.put("errorMessages", errorMessages.subList(0, Math.min(10, errorMessages.size())));
        }
        return result;
    }

    /**
     * Sync a single launch vehicle from Truth Ledger entity
     */
    private boolean syncLaunchVehicle(EntityListResponseDto.TruthLedgerEntityDto entity) {
        String name = entity.getCanonicalName();

        Optional<LaunchVehicle> existing = launchVehicleRepository.findAll().stream()
            .filter(v -> v.getName().equalsIgnoreCase(name))
            .findFirst();

        LaunchVehicle vehicle;
        boolean isNew = existing.isEmpty();

        if (isNew) {
            vehicle = new LaunchVehicle();
            vehicle.setName(name);
        } else {
            vehicle = existing.get();
        }

        // Try to get facts for this entity
        if (entity.getId() != null) {
            try {
                Optional<EntityFactsResponseDto> factsResponse =
                    truthLedgerClient.getEntityFacts(entity.getId(), truthThreshold);

                if (factsResponse.isPresent()) {
                    applyLaunchVehicleFacts(vehicle, factsResponse.get().getFacts());
                }
            } catch (Exception e) {
                log.debug("Could not fetch facts for launch vehicle {}: {}", name, e.getMessage());
            }
        }

        // Apply metadata if available
        if (entity.getMetadata() != null) {
            applyLaunchVehicleMetadata(vehicle, entity.getMetadata());
        }

        launchVehicleRepository.save(vehicle);
        log.debug("{} launch vehicle: {}", isNew ? "Created" : "Updated", name);

        return isNew;
    }

    /**
     * Apply verified facts to a launch vehicle entity
     */
    private void applyLaunchVehicleFacts(LaunchVehicle vehicle, List<EntityFactsResponseDto.FactDto> facts) {
        if (facts == null) return;

        for (EntityFactsResponseDto.FactDto fact : facts) {
            String field = fact.getAttributePattern();
            Object value = fact.getBestValue();

            if (value == null) continue;

            try {
                switch (field) {
                    case "launch_vehicles.height_m", "height_m", "height" -> {
                        vehicle.setHeightMeters(toDouble(value));
                    }
                    case "launch_vehicles.diameter_m", "diameter_m", "diameter" -> {
                        vehicle.setDiameterMeters(toDouble(value));
                    }
                    case "launch_vehicles.mass_kg", "mass_kg", "mass" -> {
                        vehicle.setMassKg(toDouble(value));
                    }
                    case "launch_vehicles.stages", "stages" -> {
                        vehicle.setStages(toInt(value));
                    }
                    case "launch_vehicles.payload_leo_kg", "payload_leo_kg", "payload_leo" -> {
                        vehicle.setPayloadToLeoKg(toInt(value));
                    }
                    case "launch_vehicles.payload_gto_kg", "payload_gto_kg", "payload_gto" -> {
                        vehicle.setPayloadToGtoKg(toInt(value));
                    }
                    case "launch_vehicles.thrust_liftoff_kn", "thrust_liftoff_kn", "thrust" -> {
                        vehicle.setThrustAtLiftoffKn(toLong(value));
                    }
                    case "launch_vehicles.manufacturer", "manufacturer" -> {
                        vehicle.setManufacturer(value.toString());
                    }
                    case "launch_vehicles.country", "country", "origin" -> {
                        linkVehicleToCountry(vehicle, value.toString());
                    }
                    case "launch_vehicles.status", "status" -> {
                        vehicle.setStatus(value.toString());
                    }
                    case "launch_vehicles.first_flight_year", "first_flight_year", "first_flight" -> {
                        vehicle.setFirstFlightYear(toInt(value));
                    }
                    case "launch_vehicles.reusable", "reusable" -> {
                        vehicle.setReusable(toBoolean(value));
                    }
                    case "launch_vehicles.human_rated", "human_rated" -> {
                        vehicle.setHumanRated(toBoolean(value));
                    }
                    case "launch_vehicles.cost_per_launch_usd", "cost_per_launch_usd", "cost" -> {
                        vehicle.setCostPerLaunchUsd(toBigDecimal(value));
                    }
                    case "launch_vehicles.description", "description" -> {
                        vehicle.setDescription(value.toString());
                    }
                    case "launch_vehicles.propellant", "propellant" -> {
                        vehicle.setPropellant(value.toString());
                    }
                    case "launch_vehicles.family", "family" -> {
                        vehicle.setFamily(value.toString());
                    }
                }
            } catch (Exception e) {
                log.debug("Could not apply fact {} = {} to launch vehicle: {}", field, value, e.getMessage());
            }
        }
    }

    /**
     * Apply metadata from Truth Ledger entity to launch vehicle
     */
    private void applyLaunchVehicleMetadata(LaunchVehicle vehicle, Map<String, Object> metadata) {
        if (metadata.containsKey("manufacturer")) {
            vehicle.setManufacturer(metadata.get("manufacturer").toString());
        }
        if (metadata.containsKey("country")) {
            linkVehicleToCountry(vehicle, metadata.get("country").toString());
        }
        if (metadata.containsKey("status")) {
            vehicle.setStatus(metadata.get("status").toString());
        }
        if (metadata.containsKey("family")) {
            vehicle.setFamily(metadata.get("family").toString());
        }
    }

    /**
     * Sync all entity types from Truth Ledger
     */
    @Transactional
    public Map<String, Object> syncAll() {
        Map<String, Object> results = new HashMap<>();

        results.put("engines", syncEngines());
        results.put("launchVehicles", syncLaunchVehicles());
        results.put("status", "success");
        results.put("message", "Full sync from Truth Ledger completed");

        return results;
    }

    // ==================== Helper Methods ====================

    private void linkEngineToCountry(Engine engine, String countryName) {
        countryRepository.findAll().stream()
            .filter(c -> c.getName().equalsIgnoreCase(countryName) ||
                         c.getCode().equalsIgnoreCase(countryName))
            .findFirst()
            .ifPresent(engine::setCountry);
    }

    private void linkVehicleToCountry(LaunchVehicle vehicle, String countryName) {
        countryRepository.findAll().stream()
            .filter(c -> c.getName().equalsIgnoreCase(countryName) ||
                         c.getCode().equalsIgnoreCase(countryName))
            .findFirst()
            .ifPresent(vehicle::setCountry);
    }

    private Double toDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.parseDouble(value.toString());
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }

    private Integer toInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private Boolean toBoolean(Object value) {
        if (value == null) return null;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return new BigDecimal(value.toString());
    }
}
