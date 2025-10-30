package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.repository.UserRepository;
import com.mig.sales.leadmanagement.exception.ResourceNotFoundException;
import com.mig.sales.leadmanagement.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for user management operations
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new user
     * @param user the user to create
     * @return created user
     */
    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already exists: " + user.getUsername());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        user.setCreatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Find user by ID
     * @param id user ID
     * @return user if found
     * @throws ResourceNotFoundException if user not found
     */
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Find user by username
     * @param username username
     * @return user if found
     * @throws ResourceNotFoundException if user not found
     */
    @Cacheable(value = "users", key = "#username")
    public User findByUsername(String username) {
        return userRepository.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Find all active users
     * @return list of active users
     */
    @Cacheable(value = "users", key = "'active'")
    public List<User> findAllActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    /**
     * Find active users by role
     * @param role user role
     * @return list of active users with the specified role
     */
    @Cacheable(value = "users", key = "#role")
    public List<User> findActiveUsersByRole(String role) {
        return userRepository.findByRoleAndIsActive(role, true);
    }

    /**
     * Find active sales persons for lead distribution
     * @return list of active sales persons
     */
    @Cacheable(value = "users", key = "'sales_persons'")
    public List<User> findActiveSalesPersons() {
        return userRepository.findActiveUsersByRole("SALES_PERSON");
    }

    /**
     * Update user
     * @param user user to update
     * @return updated user
     */
    public User updateUser(User user) {
        User existingUser = findById(user.getId());
        
        // Update fields
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setIsActive(user.getIsActive());
        
        return userRepository.save(existingUser);
    }

    /**
     * Update user password
     * @param userId user ID
     * @param newPassword new password
     */
    public void updatePassword(Long userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Update last login date
     * @param userId user ID
     */
    public void updateLastLoginDate(Long userId) {
        User user = findById(userId);
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Deactivate user
     * @param userId user ID
     */
    public void deactivateUser(Long userId) {
        User user = findById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Count active users by role
     * @param role user role
     * @return count of active users with the specified role
     */
    public long countActiveUsersByRole(String role) {
        return userRepository.countActiveUsersByRole(role);
    }

    /**
     * Validate user credentials
     * @param username username
     * @param password password
     * @return user if credentials are valid, null otherwise
     */
    public User validateCredentials(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsernameAndIsActive(username, true);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}

