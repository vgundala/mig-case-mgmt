package com.mig.sales.leadmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * LeadHistory entity representing audit trail for lead activities
 * Maps to LEAD_HISTORY table in Oracle database
 */
@Entity
@Table(name = "LEAD_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LeadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lead_history_seq")
    @SequenceGenerator(name = "lead_history_seq", sequenceName = "LEAD_HISTORY_SEQ", allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEAD_ID", nullable = false)
    private Lead lead;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Size(max = 4000, message = "Comment text must not exceed 4000 characters")
    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @Size(max = 100, message = "Action must not exceed 100 characters")
    @Column(name = "ACTION")
    private String action;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Size(max = 50, message = "Action type must not exceed 50 characters")
    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Size(max = 20, message = "Old status must not exceed 20 characters")
    @Column(name = "OLD_STATUS")
    private String oldStatus;

    @Size(max = 20, message = "New status must not exceed 20 characters")
    @Column(name = "NEW_STATUS")
    private String newStatus;

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

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}

