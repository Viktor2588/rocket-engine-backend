package com.rocket.comparison.integration.spacedevs;

import com.rocket.comparison.integration.spacedevs.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * API client for TheSpaceDevs API with resilience patterns (Step 1.2)
 * - Circuit Breaker: Prevents cascading failures when API is down
 * - Retry: Handles transient failures with exponential backoff
 * - Rate Limiter: Respects API rate limits (15 req/s for free tier)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceDevsApiClient {

    private static final String BASE_URL = "https://ll.thespacedevs.com/2.2.0";
    private static final String RESILIENCE_CONFIG = "spacedevs";

    private final RestTemplate restTemplate;

    /**
     * Fetch agencies with pagination
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchAgenciesFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<AgencyDto> fetchAgencies(int limit) {
        String url = BASE_URL + "/agencies/?limit=" + limit + "&type=Government";
        log.info("Fetching agencies from: {}", url);

        ResponseEntity<SpaceDevsPageResponse<AgencyDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<AgencyDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} agencies", response.getBody().getResults().size());
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    /**
     * Fetch recent past launches (completed)
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchLaunchesFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<LaunchDto> fetchLaunches(int limit) {
        String url = BASE_URL + "/launch/previous/?limit=" + limit + "&ordering=-net";
        log.info("Fetching previous launches from: {}", url);

        ResponseEntity<SpaceDevsPageResponse<LaunchDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<LaunchDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} launches", response.getBody().getResults().size());
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launches by year
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchLaunchesByYearFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<LaunchDto> fetchLaunchesByYear(int year, int limit) {
        String url = BASE_URL + "/launch/?limit=" + limit + "&net__gte=" + year + "-01-01&net__lte=" + year + "-12-31";
        log.info("Fetching launches for year {} from: {}", year, url);

        ResponseEntity<SpaceDevsPageResponse<LaunchDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<LaunchDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} launches for year {}", response.getBody().getResults().size(), year);
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launcher configurations (rockets)
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchLauncherConfigsFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<LauncherConfigDto> fetchLauncherConfigs(int limit) {
        String url = BASE_URL + "/config/launcher/?limit=" + limit;
        log.info("Fetching launcher configs from: {}", url);

        ResponseEntity<SpaceDevsPageResponse<LauncherConfigDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<LauncherConfigDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} launcher configs", response.getBody().getResults().size());
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launch pads
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchPadsFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<PadDto> fetchPads(int limit) {
        String url = BASE_URL + "/pad/?limit=" + limit;
        log.info("Fetching pads from: {}", url);

        ResponseEntity<SpaceDevsPageResponse<PadDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<PadDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} pads", response.getBody().getResults().size());
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    /**
     * Fetch upcoming launches
     */
    @CircuitBreaker(name = RESILIENCE_CONFIG, fallbackMethod = "fetchUpcomingLaunchesFallback")
    @Retry(name = RESILIENCE_CONFIG)
    @RateLimiter(name = RESILIENCE_CONFIG)
    public List<LaunchDto> fetchUpcomingLaunches(int limit) {
        String url = BASE_URL + "/launch/upcoming/?limit=" + limit;
        log.info("Fetching upcoming launches from: {}", url);

        ResponseEntity<SpaceDevsPageResponse<LaunchDto>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<SpaceDevsPageResponse<LaunchDto>>() {}
        );

        if (response.getBody() != null && response.getBody().getResults() != null) {
            log.info("Fetched {} upcoming launches", response.getBody().getResults().size());
            return response.getBody().getResults();
        }

        return new ArrayList<>();
    }

    // ==================== Fallback Methods ====================
    // These are called when circuit breaker is open or all retries are exhausted

    @SuppressWarnings("unused")
    private List<AgencyDto> fetchAgenciesFallback(int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchAgencies. Returning empty list. Error: {}", e.getMessage());
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    private List<LaunchDto> fetchLaunchesFallback(int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchLaunches. Returning empty list. Error: {}", e.getMessage());
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    private List<LaunchDto> fetchLaunchesByYearFallback(int year, int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchLaunchesByYear({}). Returning empty list. Error: {}", year, e.getMessage());
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    private List<LauncherConfigDto> fetchLauncherConfigsFallback(int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchLauncherConfigs. Returning empty list. Error: {}", e.getMessage());
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    private List<PadDto> fetchPadsFallback(int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchPads. Returning empty list. Error: {}", e.getMessage());
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    private List<LaunchDto> fetchUpcomingLaunchesFallback(int limit, Exception e) {
        log.warn("Circuit breaker triggered for fetchUpcomingLaunches. Returning empty list. Error: {}", e.getMessage());
        return new ArrayList<>();
    }
}
