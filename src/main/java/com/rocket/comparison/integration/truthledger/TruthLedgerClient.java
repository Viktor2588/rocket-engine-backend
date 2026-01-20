package com.rocket.comparison.integration.truthledger;

import com.rocket.comparison.integration.truthledger.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * API client for Truth Ledger service with resilience patterns
 * - Circuit Breaker: Prevents cascading failures when service is down
 * - Retry: Handles transient failures with exponential backoff
 * - Rate Limiter: Prevents overloading the truth-ledger service
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TruthLedgerClient {

    private static final String RESILIENCE_CONFIG = "truthledger";

    private final RestTemplate restTemplate;

    @Value("${truthledger.base-url:http://localhost:3000/api/v1}")
    private String baseUrl;

    /**
     * Resolve a fact by claim key hash
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "resolveFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public Optional<FactResponseDto> resolveFact(String claimKeyHash, double truthSlider) {
        String url = baseUrl + "/facts/" + claimKeyHash + "?truth_slider=" + truthSlider;
        log.debug("Resolving fact from Truth Ledger: {}", url);

        try {
            ResponseEntity<FactResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                FactResponseDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.warn("Failed to resolve fact {}: {}", claimKeyHash, e.getMessage());
            throw e;
        }
    }

    /**
     * Get fact for an engine field by domain ID
     * Example: Get ISP for engine with ID 5
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "resolveEngineFieldFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public Optional<FactResponseDto> resolveEngineField(Long engineId, String fieldName, double truthSlider) {
        String url = String.format("%s/entities/engine/%d/field/%s?truth_slider=%f",
            baseUrl, engineId, fieldName, truthSlider);
        log.debug("Resolving engine field from Truth Ledger: {}", url);

        try {
            ResponseEntity<FactResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                FactResponseDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.warn("Failed to resolve engine {} field {}: {}", engineId, fieldName, e.getMessage());
            throw e;
        }
    }

    /**
     * Get all facts for an entity
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "getEntityFactsFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public Optional<EntityFactsResponseDto> getEntityFacts(String entityId, double truthSlider) {
        String url = String.format("%s/entities/%s/facts?truth_min=%f", baseUrl, entityId, truthSlider);
        log.debug("Getting entity facts from Truth Ledger: {}", url);

        try {
            ResponseEntity<EntityFactsResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                EntityFactsResponseDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.warn("Failed to get facts for entity {}: {}", entityId, e.getMessage());
            throw e;
        }
    }

    /**
     * Get conflict groups, optionally filtered by entity
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "getConflictGroupsFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<ConflictGroupDto> getConflictGroups(String entityId, int limit) {
        String url = entityId != null
            ? String.format("%s/conflict-groups?entity_id=%s&limit=%d", baseUrl, entityId, limit)
            : String.format("%s/conflict-groups?limit=%d", baseUrl, limit);
        log.debug("Getting conflict groups from Truth Ledger: {}", url);

        try {
            ResponseEntity<List<ConflictGroupDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ConflictGroupDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.warn("Failed to get conflict groups: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Get a specific conflict group by ID
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "getConflictGroupFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public Optional<ConflictGroupDto> getConflictGroup(String conflictGroupId) {
        String url = baseUrl + "/conflict-groups/" + conflictGroupId;
        log.debug("Getting conflict group from Truth Ledger: {}", url);

        try {
            ResponseEntity<ConflictGroupDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ConflictGroupDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.warn("Failed to get conflict group {}: {}", conflictGroupId, e.getMessage());
            throw e;
        }
    }

    /**
     * Find entity by engine ID (to get the Truth Ledger entity UUID)
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "findEntityByEngineIdFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public Optional<EntityFactsResponseDto.EntityDto> findEntityByEngineId(Long engineId) {
        String url = String.format("%s/entities?type=engine&engine_id=%d", baseUrl, engineId);
        log.debug("Finding entity by engine ID from Truth Ledger: {}", url);

        try {
            ResponseEntity<List<EntityFactsResponseDto.EntityDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EntityFactsResponseDto.EntityDto>>() {}
            );
            List<EntityFactsResponseDto.EntityDto> entities = response.getBody();
            return entities != null && !entities.isEmpty()
                ? Optional.of(entities.get(0))
                : Optional.empty();
        } catch (Exception e) {
            log.warn("Failed to find entity for engine {}: {}", engineId, e.getMessage());
            throw e;
        }
    }

    /**
     * List all entities from Truth Ledger with optional filtering
     * @param entityType Optional filter by type (engine, launch_vehicle, etc.)
     * @param limit Maximum number of entities to return
     * @param offset Pagination offset
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "listEntitiesFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public EntityListResponseDto listEntities(String entityType, int limit, int offset) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl).append("/entities?");
        urlBuilder.append("limit=").append(limit);
        urlBuilder.append("&offset=").append(offset);
        if (entityType != null && !entityType.isEmpty()) {
            urlBuilder.append("&type=").append(entityType);
        }
        String url = urlBuilder.toString();
        log.debug("Listing entities from Truth Ledger: {}", url);

        try {
            ResponseEntity<EntityListResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                EntityListResponseDto.class
            );
            return response.getBody() != null ? response.getBody() : new EntityListResponseDto();
        } catch (Exception e) {
            log.warn("Failed to list entities: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * List all entities of a specific type (pages through all results)
     */
    public List<EntityListResponseDto.TruthLedgerEntityDto> listAllEntitiesByType(String entityType) {
        List<EntityListResponseDto.TruthLedgerEntityDto> allEntities = new java.util.ArrayList<>();
        int offset = 0;
        int limit = 100;

        while (true) {
            EntityListResponseDto response = listEntities(entityType, limit, offset);
            if (response.getEntities() == null || response.getEntities().isEmpty()) {
                break;
            }
            allEntities.addAll(response.getEntities());
            offset += limit;

            // Safety check - if we got less than limit, we're done
            if (response.getEntities().size() < limit) {
                break;
            }
        }

        log.info("Retrieved {} total {} entities from Truth Ledger", allEntities.size(), entityType);
        return allEntities;
    }

    /**
     * Health check for Truth Ledger service
     */
    public boolean isHealthy() {
        try {
            String url = baseUrl + "/health";
            restTemplate.getForEntity(url, String.class);
            return true;
        } catch (Exception e) {
            log.warn("Truth Ledger health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get the base URL (for debugging)
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    // ==================== Fallback Methods ====================

    @SuppressWarnings("unused")
    private Optional<FactResponseDto> resolveFallback(String claimKeyHash, double truthSlider, Exception e) {
        log.warn("Circuit breaker triggered for resolveFact({}). Error: {}", claimKeyHash, e.getMessage());
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private Optional<FactResponseDto> resolveEngineFieldFallback(Long engineId, String fieldName, double truthSlider, Exception e) {
        log.warn("Circuit breaker triggered for resolveEngineField({}, {}). Error: {}", engineId, fieldName, e.getMessage());
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private Optional<EntityFactsResponseDto> getEntityFactsFallback(String entityId, double truthSlider, Exception e) {
        log.warn("Circuit breaker triggered for getEntityFacts({}). Error: {}", entityId, e.getMessage());
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private List<ConflictGroupDto> getConflictGroupsFallback(String entityId, int limit, Exception e) {
        log.warn("Circuit breaker triggered for getConflictGroups. Error: {}", e.getMessage());
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    private Optional<ConflictGroupDto> getConflictGroupFallback(String conflictGroupId, Exception e) {
        log.warn("Circuit breaker triggered for getConflictGroup({}). Error: {}", conflictGroupId, e.getMessage());
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private Optional<EntityFactsResponseDto.EntityDto> findEntityByEngineIdFallback(Long engineId, Exception e) {
        log.warn("Circuit breaker triggered for findEntityByEngineId({}). Error: {}", engineId, e.getMessage());
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    private EntityListResponseDto listEntitiesFallback(String entityType, int limit, int offset, Exception e) {
        log.warn("Circuit breaker triggered for listEntities(type={}, limit={}, offset={}). Error: {}",
            entityType, limit, offset, e.getMessage());
        return new EntityListResponseDto();
    }
}
