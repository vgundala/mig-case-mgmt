package com.mig.sales.leadmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for Swagger documentation
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lead Management Service API")
                        .description("REST API for lead management system integration with Pega platform")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Mega Investment Group")
                                .email("support@mig.com")
                                .url("https://www.mig.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("https://leads-api.ivdata.dev/")
                                .description("Production server"),
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Local development server")));
    }
}

