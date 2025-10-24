package com.mig.sales.api.repository;

import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by username and active status.
     */
    Optional<User> findByUsernameAndIsActiveTrue(String username);

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all users by role.
     */
    List<User> findByRole(UserRole role);

    /**
     * Find all active users by role.
     */
    List<User> findByRoleAndIsActiveTrue(UserRole role);

    /**
     * Find all active users.
     */
    List<User> findByIsActiveTrue();

    /**
     * Find all users with pagination.
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Find all active users with pagination.
     */
    Page<User> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all users by role with pagination.
     */
    Page<User> findByRole(UserRole role, Pageable pageable);

    /**
     * Find all active users by role with pagination.
     */
    Page<User> findByRoleAndIsActiveTrue(UserRole role, Pageable pageable);

    /**
     * Search users by username, email, first name, or last name.
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Search active users by username, email, first name, or last name.
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND (" +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchActiveUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Check if username exists.
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists excluding a specific user.
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * Check if email exists excluding a specific user.
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Count users by role.
     */
    long countByRole(UserRole role);

    /**
     * Count active users by role.
     */
    long countByRoleAndIsActiveTrue(UserRole role);

    /**
     * Count all active users.
     */
    long countByIsActiveTrue();
}
