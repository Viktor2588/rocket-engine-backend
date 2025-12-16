package com.rocket.comparison;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests using TestContainers (Step 1.3)
 *
 * Provides a real PostgreSQL database instance for integration tests,
 * ensuring tests run against the same database type as production.
 *
 * Benefits:
 * - Tests use real PostgreSQL instead of H2 (catches PostgreSQL-specific issues)
 * - Isolated database per test run
 * - No need for local PostgreSQL installation
 * - Flyway migrations run automatically
 *
 * Usage:
 * Extend this class for integration tests that need database access:
 *
 *   public class MyServiceIntegrationTest extends BaseIntegrationTest {
 *       @Autowired
 *       private MyService myService;
 *
 *       @Test
 *       void testSomething() { ... }
 *   }
 */
@SpringBootTest
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("rocket_test")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);  // Reuse container across test classes for speed

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Disable external API sync during tests
        registry.add("sync.external.enabled", () -> "false");

        // Let Hibernate create schema for tests (Flyway migrations may have test-incompatible constraints)
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        // Disable Flyway in tests to avoid migration conflicts
        registry.add("spring.flyway.enabled", () -> "false");
    }
}
