package com.rocket.comparison.integration.spacedevs;

import com.rocket.comparison.integration.spacedevs.dto.*;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceDevsApiClient {

    private static final String BASE_URL = "https://ll.thespacedevs.com/2.2.0";
    private static final int DEFAULT_LIMIT = 100;

    private final RestTemplate restTemplate;

    /**
     * Fetch agencies with pagination
     */
    public List<AgencyDto> fetchAgencies(int limit) {
        String url = BASE_URL + "/agencies/?limit=" + limit + "&type=Government";
        log.info("Fetching agencies from: {}", url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching agencies: {}", e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * Fetch recent launches
     */
    public List<LaunchDto> fetchLaunches(int limit) {
        String url = BASE_URL + "/launch/?limit=" + limit + "&ordering=-net";
        log.info("Fetching launches from: {}", url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching launches: {}", e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launches by year
     */
    public List<LaunchDto> fetchLaunchesByYear(int year, int limit) {
        String url = BASE_URL + "/launch/?limit=" + limit + "&net__gte=" + year + "-01-01&net__lte=" + year + "-12-31";
        log.info("Fetching launches for year {} from: {}", year, url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching launches for year {}: {}", year, e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launcher configurations (rockets)
     */
    public List<LauncherConfigDto> fetchLauncherConfigs(int limit) {
        String url = BASE_URL + "/config/launcher/?limit=" + limit;
        log.info("Fetching launcher configs from: {}", url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching launcher configs: {}", e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * Fetch launch pads
     */
    public List<PadDto> fetchPads(int limit) {
        String url = BASE_URL + "/pad/?limit=" + limit;
        log.info("Fetching pads from: {}", url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching pads: {}", e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * Fetch upcoming launches
     */
    public List<LaunchDto> fetchUpcomingLaunches(int limit) {
        String url = BASE_URL + "/launch/upcoming/?limit=" + limit;
        log.info("Fetching upcoming launches from: {}", url);

        try {
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
        } catch (RestClientException e) {
            log.error("Error fetching upcoming launches: {}", e.getMessage());
        }

        return new ArrayList<>();
    }
}
