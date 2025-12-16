package com.rocket.comparison.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration (Step 3.1)
 * Provides auto-generated API documentation at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Rocket Engine Comparison API")
                .description("""
                    A comprehensive API for comparing rocket engines, launch vehicles,
                    space missions, and analyzing space programs across countries.

                    ## Features
                    - **Engines**: Compare rocket engines by thrust, ISP, propellant type
                    - **Countries**: Explore space agencies and their capabilities
                    - **Launch Vehicles**: View rockets and their specifications
                    - **Missions**: Track past, current, and planned space missions
                    - **Analytics**: Access statistics and trend data

                    ## Data Sources
                    - Curated seed data for major space programs
                    - Real-time sync from TheSpaceDevs API
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@example.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local Development"),
                new Server().url("https://api.example.com").description("Production")))
            .tags(List.of(
                new Tag().name("Engines").description("Rocket engine endpoints"),
                new Tag().name("Countries").description("Space-faring nations and agencies"),
                new Tag().name("Launch Vehicles").description("Rockets and launch systems"),
                new Tag().name("Missions").description("Space missions and launches"),
                new Tag().name("Milestones").description("Historic space achievements"),
                new Tag().name("Satellites").description("Satellites and space stations"),
                new Tag().name("Launch Sites").description("Spaceports and launch facilities"),
                new Tag().name("Analytics").description("Statistics and analytics"),
                new Tag().name("Comparison").description("Comparison and ranking endpoints"),
                new Tag().name("Sync").description("External API synchronization")
            ));
    }
}
