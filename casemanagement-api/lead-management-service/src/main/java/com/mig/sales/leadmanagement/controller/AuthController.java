package com.mig.sales.leadmanagement.controller;

import com.mig.sales.leadmanagement.dto.LoginRequest;
import com.mig.sales.leadmanagement.dto.LoginResponse;
import com.mig.sales.leadmanagement.dto.ApiResponse;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.service.UserService;
import com.mig.sales.leadmanagement.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller for login operations
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * User login endpoint
     * @param loginRequest login credentials
     * @return JWT token and user information
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Validate credentials
            User user = userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            
            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid username or password"));
            }

            // Update last login date
            userService.updateLastLoginDate(user.getId());

            // Generate JWT token
            String token = jwtUtil.generateToken(user);

            // Create response
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setUserId(user.getId());
            loginResponse.setUsername(user.getUsername());
            loginResponse.setRole(user.getRole());
            loginResponse.setFirstName(user.getFirstName());
            loginResponse.setLastName(user.getLastName());
            loginResponse.setEmail(user.getEmail());
            loginResponse.setExpiresIn(jwtUtil.getExpirationTime());

            return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    /**
     * User registration endpoint
     * @param user user to register
     * @return created user information
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
}

