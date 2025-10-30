package com.mig.sales.leadmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for lead creation and update requests
 */
@Data
public class LeadRequest {
    
    @NotBlank(message = "Lead name is required")
    @Size(max = 100, message = "Lead name must not exceed 100 characters")
    private String leadName;
    
    @Size(max = 100, message = "Company must not exceed 100 characters")
    private String company;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    @NotNull(message = "Potential value is required")
    @Positive(message = "Potential value must be positive")
    private BigDecimal potentialValue;
    
    @Size(max = 50, message = "Lead source must not exceed 50 characters")
    private String leadSource;
    
    private String description;
    
    @Size(max = 50, message = "Industry must not exceed 50 characters")
    private String industry;
    
    @Size(max = 20, message = "Company size must not exceed 20 characters")
    private String companySize;
    
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
}

