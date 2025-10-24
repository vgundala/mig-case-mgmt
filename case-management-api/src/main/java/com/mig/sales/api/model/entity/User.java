package com.mig.sales.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing system users (Sales Person or Sales Manager).
 */
@Entity
@Table(name = "APP_USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_users_seq")
    @SequenceGenerator(name = "app_users_seq", sequenceName = "APP_USERS_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private UserRole role;

    @Email(message = "Email should be valid")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "IS_ACTIVE")
    @Builder.Default
    private Boolean isActive = true;

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

    // Relationships
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Lead> assignedLeads = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LeadHistory> leadHistory = new ArrayList<>();

    // Helper methods
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }

    public boolean isSalesPerson() {
        return UserRole.SALES_PERSON.equals(this.role);
    }

    public boolean isSalesManager() {
        return UserRole.SALES_MANAGER.equals(this.role);
    }
}
