package com.rocket.comparison.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to log deprecation warnings for unpaged endpoints (BE-002/003)
 */
@Slf4j
@Component
public class DeprecationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        // Check for ?unpaged=true
        if (queryString != null && queryString.contains("unpaged=true")) {
            log.warn("DEPRECATION: unpaged=true used on {} from IP {}. " +
                    "This parameter will be removed. Use pagination instead.",
                    uri, request.getRemoteAddr());
            response.addHeader("X-Deprecation-Warning",
                    "unpaged=true is deprecated and will be removed. Use pagination parameters instead.");
        }

        // Check for /all or /list endpoints
        if (uri.endsWith("/all") || uri.endsWith("/list")) {
            log.warn("DEPRECATION: Unbounded endpoint {} called from IP {}. " +
                    "These endpoints will be removed. Use paginated endpoints instead.",
                    uri, request.getRemoteAddr());
            response.addHeader("X-Deprecation-Warning",
                    "This endpoint is deprecated. Use the paginated base endpoint instead.");
        }

        return true;
    }
}
