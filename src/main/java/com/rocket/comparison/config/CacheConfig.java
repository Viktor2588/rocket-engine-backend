package com.rocket.comparison.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration using Caffeine (BE-055)
 * Provides in-memory caching for analytics and expensive queries
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String ANALYTICS_CACHE = "analytics";
    public static final String STATISTICS_CACHE = "statistics";
    public static final String RANKINGS_CACHE = "rankings";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats());
        cacheManager.setCacheNames(java.util.List.of(
                ANALYTICS_CACHE,
                STATISTICS_CACHE,
                RANKINGS_CACHE
        ));
        return cacheManager;
    }
}
