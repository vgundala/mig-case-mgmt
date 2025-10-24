package com.mig.sales.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for Case Management API.
 * 
 * This application provides a modern REST API for lead management
 * with Pega workflow integration and JWT authentication.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class CaseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseManagementApplication.class, args);
    }
}
