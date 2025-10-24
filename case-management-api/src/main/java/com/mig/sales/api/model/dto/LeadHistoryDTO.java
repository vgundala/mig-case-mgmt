package com.mig.sales.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for LeadHistory entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadHistoryDTO {

    private Long id;
    private Long leadId;
    private UserDTO user;
    private String commentText;
    private String action;
    private LocalDateTime timestamp;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
