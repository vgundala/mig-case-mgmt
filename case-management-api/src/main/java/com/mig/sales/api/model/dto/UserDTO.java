package com.mig.sales.api.model.dto;

import com.mig.sales.api.model.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
}
