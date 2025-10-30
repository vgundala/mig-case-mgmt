package com.mig.sales.leadmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for Lead Management Service
 * 
 * This microservice provides REST APIs for lead management operations
 * and integrates with Pega platform for workflow automation.
 */
@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class LeadManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeadManagementServiceApplication.class, args);
    }
}

