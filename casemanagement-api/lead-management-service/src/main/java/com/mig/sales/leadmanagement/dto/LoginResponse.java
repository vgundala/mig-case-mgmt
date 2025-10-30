package com.mig.sales.leadmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private Long expiresIn;
}

