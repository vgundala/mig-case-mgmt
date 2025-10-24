package com.mig.sales.api.model.dto;

import com.mig.sales.api.model.entity.LeadSource;
import com.mig.sales.api.model.entity.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Lead entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadDTO {

    private Long id;
    private String leadName;
    private String company;
    private String email;
    private String phone;
    private LeadStatus status;
    private UserDTO assignedTo;
    private BigDecimal potentialValue;
    private LeadSource leadSource;
    private Integer leadScore;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
    
    // Pega Integration Fields
    private String pegaWorkflowId;
    private String pegaCaseId;
    private String pegaStatus;
    private LocalDateTime pegaLastSyncDate;
}
