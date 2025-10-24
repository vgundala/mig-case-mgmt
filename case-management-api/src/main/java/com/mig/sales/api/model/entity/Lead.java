package com.mig.sales.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lead entity representing potential clients in the sales pipeline.
 */
@Entity
@Table(name = "LEADS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leads_seq")
    @SequenceGenerator(name = "leads_seq", sequenceName = "LEADS_SEQ", allocationSize = 1)
    @Column(name = "LEAD_ID")
    private Long id;

    @NotBlank(message = "Lead name is required")
    @Size(max = 100, message = "Lead name must not exceed 100 characters")
    @Column(name = "LEAD_NAME", nullable = false)
    private String leadName;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    @Column(name = "COMPANY")
    private String company;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "EMAIL")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "PHONE")
    private String phone;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private LeadStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private User assignedTo;

    @DecimalMin(value = "0.0", message = "Potential value must be positive")
    @Digits(integer = 12, fraction = 2, message = "Potential value must have at most 12 integer digits and 2 decimal places")
    @Column(name = "POTENTIAL_VALUE", precision = 12, scale = 2)
    private BigDecimal potentialValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "LEAD_SOURCE")
    private LeadSource leadSource;

    @Min(value = 0, message = "Lead score must be non-negative")
    @Max(value = 100, message = "Lead score cannot exceed 100")
    @Column(name = "LEAD_SCORE")
    private Integer leadScore;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    // Pega Integration Fields
    @Size(max = 100, message = "Pega workflow ID must not exceed 100 characters")
    @Column(name = "PEGA_WORKFLOW_ID")
    private String pegaWorkflowId;

    @Size(max = 100, message = "Pega case ID must not exceed 100 characters")
    @Column(name = "PEGA_CASE_ID")
    private String pegaCaseId;

    @Size(max = 50, message = "Pega status must not exceed 50 characters")
    @Column(name = "PEGA_STATUS")
    private String pegaStatus;

    @Column(name = "PEGA_LAST_SYNC_DATE")
    private LocalDateTime pegaLastSyncDate;

    // Relationships
    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LeadHistory> history = new ArrayList<>();

    // Helper methods
    public boolean isNew() {
        return LeadStatus.NEW.equals(this.status);
    }

    public boolean isAssigned() {
        return LeadStatus.ASSIGNED.equals(this.status);
    }

    public boolean isInProgress() {
        return LeadStatus.IN_PROGRESS.equals(this.status);
    }

    public boolean isPreConversion() {
        return LeadStatus.PRE_CONVERSION.equals(this.status);
    }

    public boolean isConverted() {
        return LeadStatus.CONVERTED.equals(this.status);
    }

    public boolean isRejected() {
        return LeadStatus.REJECTED.equals(this.status);
    }

    public boolean isHighValue() {
        return potentialValue != null && potentialValue.compareTo(new BigDecimal("1000000")) > 0;
    }

    public boolean hasPegaWorkflow() {
        return pegaWorkflowId != null && !pegaWorkflowId.trim().isEmpty();
    }
}
