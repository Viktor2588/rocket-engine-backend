package com.rocket.comparison.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async configuration for background tasks (Step 2.3)
 * Prevents sync operations from blocking HTTP request threads
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    /**
     * Custom thread pool for sync operations.
     * Separate from the HTTP request thread pool to prevent resource starvation.
     */
    @Bean(name = "syncTaskExecutor")
    public Executor syncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);      // Minimum threads
        executor.setMaxPoolSize(4);       // Maximum threads
        executor.setQueueCapacity(50);    // Queue size before rejecting
        executor.setThreadNamePrefix("sync-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler((r, e) ->
            log.warn("Sync task rejected - queue full. Consider increasing capacity."));
        executor.initialize();

        log.info("Initialized sync task executor: core={}, max={}, queue={}",
            executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}
