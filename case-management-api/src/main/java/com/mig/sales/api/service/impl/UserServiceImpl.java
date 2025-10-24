package com.mig.sales.api.service.impl;

import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.entity.UserRole;
import com.mig.sales.api.model.mapper.UserMapper;
import com.mig.sales.api.repository.UserRepository;
import com.mig.sales.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDTO.getUsername());
        }
        
        // Check if email already exists
        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }
        
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUsername());
        
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        log.debug("Getting user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByUsername(String username) {
        log.debug("Getting user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.debug("Getting all users with pagination");
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getActiveUsers(Pageable pageable) {
        log.debug("Getting active users with pagination");
        return userRepository.findByIsActiveTrue(pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsersByRole(UserRole role, Pageable pageable) {
        log.debug("Getting users by role: {} with pagination", role);
        return userRepository.findByRole(role, pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getActiveUsersByRole(UserRole role, Pageable pageable) {
        log.debug("Getting active users by role: {} with pagination", role);
        return userRepository.findByRoleAndIsActiveTrue(role, pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers(String searchTerm, Pageable pageable) {
        log.debug("Searching users with term: {}", searchTerm);
        return userRepository.searchUsers(searchTerm, pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> searchActiveUsers(String searchTerm, Pageable pageable) {
        log.debug("Searching active users with term: {}", searchTerm);
        return userRepository.searchActiveUsers(searchTerm, pageable)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if username already exists (excluding current user)
        if (!existingUser.getUsername().equals(userDTO.getUsername()) && 
            userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), id)) {
            throw new RuntimeException("Username already exists: " + userDTO.getUsername());
        }
        
        // Check if email already exists (excluding current user)
        if (userDTO.getEmail() != null && 
            !userDTO.getEmail().equals(existingUser.getEmail()) && 
            userRepository.existsByEmailAndIdNot(userDTO.getEmail(), id)) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }
        
        // Update fields
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setRole(userDTO.getRole());
        
        // Update password if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        User savedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", savedUser.getUsername());
        
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        log.info("User deactivated successfully: {}", user.getUsername());
    }

    @Override
    @Transactional
    public UserDTO activateUser(Long id) {
        log.info("Activating user: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        
        log.info("User activated successfully: {}", savedUser.getUsername());
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO deactivateUser(Long id) {
        log.info("Deactivating user: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setIsActive(false);
        User savedUser = userRepository.save(user);
        
        log.info("User deactivated successfully: {}", savedUser.getUsername());
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getSalesPeople() {
        log.debug("Getting all sales people");
        return userRepository.findByRoleAndIsActiveTrue(UserRole.SALES_PERSON)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getSalesManagers() {
        log.debug("Getting all sales managers");
        return userRepository.findByRoleAndIsActiveTrue(UserRole.SALES_MANAGER)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExistsExcludingUser(String username, Long userId) {
        return userRepository.existsByUsernameAndIdNot(username, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExistsExcludingUser(String email, Long userId) {
        return userRepository.existsByEmailAndIdNot(email, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserCountByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveUserCountByRole(UserRole role) {
        return userRepository.countByRoleAndIsActiveTrue(role);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalActiveUserCount() {
        return userRepository.countByIsActiveTrue();
    }
}
