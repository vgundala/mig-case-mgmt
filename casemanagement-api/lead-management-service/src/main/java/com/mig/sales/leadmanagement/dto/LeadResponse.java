package com.mig.sales.leadmanagement.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for lead response
 */
@Data
public class LeadResponse {
    
    private Long id;
    private String leadName;
    private String company;
    private String email;
    private String phone;
    private String status;
    private Long assignedTo;
    private String assignedToUsername;
    private String assignedToFirstName;
    private String assignedToLastName;
    private BigDecimal potentialValue;
    private String leadSource;
    private Integer leadScore;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String description;
    private String industry;
    private String companySize;
    private String location;
}

