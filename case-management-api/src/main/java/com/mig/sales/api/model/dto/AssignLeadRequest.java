package com.mig.sales.api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for assigning a lead to a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignLeadRequest {

    @NotNull(message = "Assigned user ID is required")
    private Long assignedTo;
}
