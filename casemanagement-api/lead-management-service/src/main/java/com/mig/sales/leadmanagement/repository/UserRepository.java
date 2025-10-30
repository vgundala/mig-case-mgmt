package com.mig.sales.leadmanagement.repository;

import com.mig.sales.leadmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Provides data access methods for user operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by username and active status
     * @param username the username to search for
     * @param isActive the active status
     * @return Optional containing the user if found
     */
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    /**
     * Find all active users by role
     * @param role the role to search for
     * @param isActive the active status
     * @return List of users with the specified role and active status
     */
    List<User> findByRoleAndIsActive(String role, Boolean isActive);

    /**
     * Find all active users
     * @param isActive the active status
     * @return List of all active users
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Check if username exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Find users by role for lead distribution
     * @param role the role (SALES_PERSON)
     * @return List of users with the specified role
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true ORDER BY u.id")
    List<User> findActiveUsersByRole(@Param("role") String role);

    /**
     * Count active users by role
     * @param role the role to count
     * @return number of active users with the specified role
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    long countActiveUsersByRole(@Param("role") String role);
}

