package com.mig.sales.api.model.dto;

import com.mig.sales.api.model.entity.LeadSource;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new lead.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeadRequest {

    @NotBlank(message = "Lead name is required")
    @Size(max = 100, message = "Lead name must not exceed 100 characters")
    private String leadName;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @DecimalMin(value = "0.0", message = "Potential value must be positive")
    @Digits(integer = 12, fraction = 2, message = "Potential value must have at most 12 integer digits and 2 decimal places")
    private BigDecimal potentialValue;

    private LeadSource leadSource;
}
