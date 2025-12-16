package com.rocket.comparison.config;

import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator showing database connection and data status (BE-080)
 */
@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;

    @Override
    public Health health() {
        try {
            long countryCount = countryRepository.count();
            long engineCount = engineRepository.count();

            return Health.up()
                    .withDetail("status", "Database connection healthy")
                    .withDetail("countries", countryCount)
                    .withDetail("engines", engineCount)
                    .withDetail("dataLoaded", countryCount > 0 || engineCount > 0)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Database connection failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
