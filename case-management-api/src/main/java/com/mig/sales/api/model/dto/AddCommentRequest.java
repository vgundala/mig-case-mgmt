package com.mig.sales.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding a comment to a lead.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentRequest {

    @NotBlank(message = "Comment text is required")
    @Size(max = 4000, message = "Comment text must not exceed 4000 characters")
    private String commentText;

    @NotBlank(message = "Action is required")
    @Size(max = 100, message = "Action must not exceed 100 characters")
    private String action;
}
