package com.mig.sales.leadmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lead entity representing potential clients
 * Maps to LEADS table in Oracle database
 */
@Entity
@Table(name = "LEADS")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Size(max = 100, message = "Company must not exceed 100 characters")
    @Column(name = "COMPANY")
    private String company;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "PHONE")
    private String phone;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Column(name = "STATUS", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private User assignedTo;

    @Column(name = "POTENTIAL_VALUE", precision = 12, scale = 2)
    private BigDecimal potentialValue;

    @Size(max = 50, message = "Lead source must not exceed 50 characters")
    @Column(name = "LEAD_SOURCE")
    private String leadSource;

    @Column(name = "LEAD_SCORE")
    private Integer leadScore;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 50, message = "Industry must not exceed 50 characters")
    @Column(name = "INDUSTRY")
    private String industry;

    @Size(max = 20, message = "Company size must not exceed 20 characters")
    @Column(name = "COMPANY_SIZE")
    private String companySize;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    @Column(name = "LOCATION")
    private String location;

    // Pega audit fields
    @Column(name = "PX_CREATED_DATETIME")
    @CreatedDate
    private LocalDateTime pxCreatedDateTime;

    @Column(name = "PX_UPDATED_DATETIME")
    @LastModifiedDate
    private LocalDateTime pxUpdatedDateTime;

    @Column(name = "PX_CREATED_BY")
    private String pxCreatedBy;

    @Column(name = "PX_UPDATED_BY")
    private String pxUpdatedBy;

    // Relationships
    @OneToMany(mappedBy = "lead", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeadHistory> leadHistories;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (updatedDate == null) {
            updatedDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

