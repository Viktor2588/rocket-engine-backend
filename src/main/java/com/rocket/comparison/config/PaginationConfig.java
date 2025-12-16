package com.rocket.comparison.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Global pagination configuration (BE-001)
 * Enforces consistent pagination limits across all endpoints
 */
@Configuration
public class PaginationConfig implements WebMvcConfigurer {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, DEFAULT_PAGE_SIZE));
        resolver.setMaxPageSize(MAX_PAGE_SIZE);
        resolvers.add(resolver);
    }
}
