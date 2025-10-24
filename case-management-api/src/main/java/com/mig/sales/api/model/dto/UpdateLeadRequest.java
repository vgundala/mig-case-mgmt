package com.mig.sales.api.model.dto;

import com.mig.sales.api.model.entity.LeadSource;
import com.mig.sales.api.model.entity.LeadStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for updating a lead.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLeadRequest {

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

    private LeadStatus status;

    @DecimalMin(value = "0.0", message = "Potential value must be positive")
    @Digits(integer = 12, fraction = 2, message = "Potential value must have at most 12 integer digits and 2 decimal places")
    private BigDecimal potentialValue;

    private LeadSource leadSource;

    @Min(value = 0, message = "Lead score must be non-negative")
    @Max(value = 100, message = "Lead score cannot exceed 100")
    private Integer leadScore;
}
