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

import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing system users (Sales Person or Sales Manager)
 * Maps to APP_USERS table in Oracle database
 */
@Entity
@Table(name = "APP_USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_users_seq")
    @SequenceGenerator(name = "app_users_seq", sequenceName = "APP_USERS_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 255, message = "Password must not exceed 255 characters")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    @Size(max = 20, message = "Role must not exceed 20 characters")
    @Column(name = "ROLE", nullable = false)
    private String role;

    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "LAST_NAME")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "PHONE")
    private String phone;

    @NotNull
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = true;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

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
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<Lead> assignedLeads;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LeadHistory> leadHistories;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }
}

