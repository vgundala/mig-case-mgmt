package com.mig.sales.api.service;

import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User management operations.
 */
public interface UserService {

    /**
     * Create a new user.
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Get user by ID.
     */
    Optional<UserDTO> getUserById(Long id);

    /**
     * Get user by username.
     */
    Optional<UserDTO> getUserByUsername(String username);

    /**
     * Get all users with pagination.
     */
    Page<UserDTO> getAllUsers(Pageable pageable);

    /**
     * Get all active users with pagination.
     */
    Page<UserDTO> getActiveUsers(Pageable pageable);

    /**
     * Get users by role with pagination.
     */
    Page<UserDTO> getUsersByRole(UserRole role, Pageable pageable);

    /**
     * Get active users by role with pagination.
     */
    Page<UserDTO> getActiveUsersByRole(UserRole role, Pageable pageable);

    /**
     * Search users by term with pagination.
     */
    Page<UserDTO> searchUsers(String searchTerm, Pageable pageable);

    /**
     * Search active users by term with pagination.
     */
    Page<UserDTO> searchActiveUsers(String searchTerm, Pageable pageable);

    /**
     * Update user.
     */
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * Delete user (soft delete).
     */
    void deleteUser(Long id);

    /**
     * Activate user.
     */
    UserDTO activateUser(Long id);

    /**
     * Deactivate user.
     */
    UserDTO deactivateUser(Long id);

    /**
     * Get all sales people.
     */
    List<UserDTO> getSalesPeople();

    /**
     * Get all sales managers.
     */
    List<UserDTO> getSalesManagers();

    /**
     * Check if username exists.
     */
    boolean usernameExists(String username);

    /**
     * Check if email exists.
     */
    boolean emailExists(String email);

    /**
     * Check if username exists excluding a specific user.
     */
    boolean usernameExistsExcludingUser(String username, Long userId);

    /**
     * Check if email exists excluding a specific user.
     */
    boolean emailExistsExcludingUser(String email, Long userId);

    /**
     * Get user count by role.
     */
    long getUserCountByRole(UserRole role);

    /**
     * Get active user count by role.
     */
    long getActiveUserCountByRole(UserRole role);

    /**
     * Get total active user count.
     */
    long getTotalActiveUserCount();
}
