package com.mig.sales.api.controller;

import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.UserRole;
import com.mig.sales.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for User management operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination and filtering")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Parameter(description = "User role filter") @RequestParam(required = false) UserRole role,
            @Parameter(description = "Active status filter") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Search term") @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting users with filters - role: {}, active: {}, search: {}", role, active, search);
        
        Page<UserDTO> users;
        
        if (search != null && !search.trim().isEmpty()) {
            if (Boolean.TRUE.equals(active)) {
                users = userService.searchActiveUsers(search, pageable);
            } else {
                users = userService.searchUsers(search, pageable);
            }
        } else if (role != null && Boolean.TRUE.equals(active)) {
            users = userService.getActiveUsersByRole(role, pageable);
        } else if (role != null) {
            users = userService.getUsersByRole(role, pageable);
        } else if (Boolean.TRUE.equals(active)) {
            users = userService.getActiveUsers(pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }
        
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.debug("Getting user by ID: {}", id);
        
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Create a new user in the system")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.getUsername());
        
        try {
            UserDTO user = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            log.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("Updating user: {}", id);
        
        try {
            UserDTO user = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.error("Error updating user: {}", e.getMessage());
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deactivate a user (soft delete)")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deactivating user: {}", id);
        
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deactivating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a deactivated user")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User activated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserDTO> activateUser(@PathVariable Long id) {
        log.info("Activating user: {}", id);
        
        try {
            UserDTO user = userService.activateUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.error("Error activating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user: {}", id);
        
        try {
            UserDTO user = userService.deactivateUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.error("Error deactivating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sales-people")
    @Operation(summary = "Get sales people", description = "Retrieve all active sales people")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sales people retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserDTO>> getSalesPeople() {
        log.debug("Getting all sales people");
        
        List<UserDTO> salesPeople = userService.getSalesPeople();
        return ResponseEntity.ok(salesPeople);
    }

    @GetMapping("/sales-managers")
    @Operation(summary = "Get sales managers", description = "Retrieve all active sales managers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sales managers retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserDTO>> getSalesManagers() {
        log.debug("Getting all sales managers");
        
        List<UserDTO> salesManagers = userService.getSalesManagers();
        return ResponseEntity.ok(salesManagers);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user statistics", description = "Get user count statistics")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<UserStatsResponse> getUserStats() {
        log.debug("Getting user statistics");
        
        UserStatsResponse stats = UserStatsResponse.builder()
                .totalUsers(userService.getTotalActiveUserCount())
                .salesPeople(userService.getActiveUserCountByRole(UserRole.SALES_PERSON))
                .salesManagers(userService.getActiveUserCountByRole(UserRole.SALES_MANAGER))
                .build();
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/check-username")
    @Operation(summary = "Check username availability", description = "Check if a username is available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Username availability checked"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<UsernameCheckResponse> checkUsername(
            @Parameter(description = "Username to check") @RequestParam String username,
            @Parameter(description = "User ID to exclude (for updates)") @RequestParam(required = false) Long excludeUserId) {
        
        log.debug("Checking username availability: {}", username);
        
        boolean available;
        if (excludeUserId != null) {
            available = !userService.usernameExistsExcludingUser(username, excludeUserId);
        } else {
            available = !userService.usernameExists(username);
        }
        
        UsernameCheckResponse response = UsernameCheckResponse.builder()
                .username(username)
                .available(available)
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if an email is available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email availability checked"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<EmailCheckResponse> checkEmail(
            @Parameter(description = "Email to check") @RequestParam String email,
            @Parameter(description = "User ID to exclude (for updates)") @RequestParam(required = false) Long excludeUserId) {
        
        log.debug("Checking email availability: {}", email);
        
        boolean available;
        if (excludeUserId != null) {
            available = !userService.emailExistsExcludingUser(email, excludeUserId);
        } else {
            available = !userService.emailExists(email);
        }
        
        EmailCheckResponse response = EmailCheckResponse.builder()
                .email(email)
                .available(available)
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Response DTO for user statistics.
     */
    public static class UserStatsResponse {
        private long totalUsers;
        private long salesPeople;
        private long salesManagers;

        public static UserStatsResponseBuilder builder() {
            return new UserStatsResponseBuilder();
        }

        public static class UserStatsResponseBuilder {
            private long totalUsers;
            private long salesPeople;
            private long salesManagers;

            public UserStatsResponseBuilder totalUsers(long totalUsers) {
                this.totalUsers = totalUsers;
                return this;
            }

            public UserStatsResponseBuilder salesPeople(long salesPeople) {
                this.salesPeople = salesPeople;
                return this;
            }

            public UserStatsResponseBuilder salesManagers(long salesManagers) {
                this.salesManagers = salesManagers;
                return this;
            }

            public UserStatsResponse build() {
                UserStatsResponse response = new UserStatsResponse();
                response.totalUsers = this.totalUsers;
                response.salesPeople = this.salesPeople;
                response.salesManagers = this.salesManagers;
                return response;
            }
        }

        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getSalesPeople() { return salesPeople; }
        public void setSalesPeople(long salesPeople) { this.salesPeople = salesPeople; }
        public long getSalesManagers() { return salesManagers; }
        public void setSalesManagers(long salesManagers) { this.salesManagers = salesManagers; }
    }

    /**
     * Response DTO for username availability check.
     */
    public static class UsernameCheckResponse {
        private String username;
        private boolean available;

        public static UsernameCheckResponseBuilder builder() {
            return new UsernameCheckResponseBuilder();
        }

        public static class UsernameCheckResponseBuilder {
            private String username;
            private boolean available;

            public UsernameCheckResponseBuilder username(String username) {
                this.username = username;
                return this;
            }

            public UsernameCheckResponseBuilder available(boolean available) {
                this.available = available;
                return this;
            }

            public UsernameCheckResponse build() {
                UsernameCheckResponse response = new UsernameCheckResponse();
                response.username = this.username;
                response.available = this.available;
                return response;
            }
        }

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }

    /**
     * Response DTO for email availability check.
     */
    public static class EmailCheckResponse {
        private String email;
        private boolean available;

        public static EmailCheckResponseBuilder builder() {
            return new EmailCheckResponseBuilder();
        }

        public static class EmailCheckResponseBuilder {
            private String email;
            private boolean available;

            public EmailCheckResponseBuilder email(String email) {
                this.email = email;
                return this;
            }

            public EmailCheckResponseBuilder available(boolean available) {
                this.available = available;
                return this;
            }

            public EmailCheckResponse build() {
                EmailCheckResponse response = new EmailCheckResponse();
                response.email = this.email;
                response.available = this.available;
                return response;
            }
        }

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }
}
