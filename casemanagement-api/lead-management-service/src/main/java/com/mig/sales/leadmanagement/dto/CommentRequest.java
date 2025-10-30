package com.mig.sales.leadmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for adding comments to leads
 */
@Data
public class CommentRequest {
    
    @NotBlank(message = "Comment text is required")
    @Size(max = 4000, message = "Comment text must not exceed 4000 characters")
    private String commentText;
    
    @Size(max = 100, message = "Action must not exceed 100 characters")
    private String action;
}

