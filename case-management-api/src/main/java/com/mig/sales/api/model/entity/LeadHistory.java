package com.mig.sales.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * LeadHistory entity representing audit trail for lead activities.
 */
@Entity
@Table(name = "LEAD_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class LeadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lead_history_seq")
    @SequenceGenerator(name = "lead_history_seq", sequenceName = "LEAD_HISTORY_SEQ", allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long id;

    @NotNull(message = "Lead is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEAD_ID", nullable = false)
    private Lead lead;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Size(max = 4000, message = "Comment text must not exceed 4000 characters")
    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @NotBlank(message = "Action is required")
    @Size(max = 100, message = "Action must not exceed 100 characters")
    @Column(name = "ACTION", nullable = false)
    private String action;

    @CreatedDate
    @Column(name = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    // Helper methods
    public boolean isComment() {
        return commentText != null && !commentText.trim().isEmpty();
    }

    public boolean isStatusChange() {
        return action != null && action.contains("STATUS");
    }

    public boolean isAssignment() {
        return action != null && action.contains("ASSIGNED");
    }
}
