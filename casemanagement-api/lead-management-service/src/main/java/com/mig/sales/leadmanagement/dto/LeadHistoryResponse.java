package com.mig.sales.leadmanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for lead history response
 */
@Data
public class LeadHistoryResponse {
    
    private Long id;
    private Long leadId;
    private Long userId;
    private String username;
    private String userFirstName;
    private String userLastName;
    private String commentText;
    private String action;
    private LocalDateTime timestamp;
    private String actionType;
    private String oldStatus;
    private String newStatus;
}

