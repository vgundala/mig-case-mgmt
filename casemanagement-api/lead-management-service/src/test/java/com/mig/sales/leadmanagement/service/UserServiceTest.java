package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.exception.BusinessException;
import com.mig.sales.leadmanagement.exception.ResourceNotFoundException;
import com.mig.sales.leadmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("hashedPassword");
        testUser.setRole("SALES_PERSON");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("555-0123");
        testUser.setIsActive(true);
        testUser.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("plainPassword");
        newUser.setRole("SALES_PERSON");
        newUser.setFirstName("New");
        newUser.setLastName("User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.createUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals("hashedPassword", result.getPassword());
        assertTrue(result.getIsActive());
        assertNotNull(result.getCreatedDate());
        verify(userRepository).existsByUsername("newuser");
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void testCreateUser_UsernameExists() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("existinguser");
        newUser.setPassword("plainPassword");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.createUser(newUser);
        });

        assertEquals("Username already exists: existinguser", exception.getMessage());
        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(999L);
        });

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        when(userRepository.findByUsernameAndIsActive("testuser", true))
                .thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository).findByUsernameAndIsActive("testuser", true);
    }

    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        when(userRepository.findByUsernameAndIsActive("nonexistent", true))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByUsername("nonexistent");
        });

        assertEquals("User not found with username: nonexistent", exception.getMessage());
        verify(userRepository).findByUsernameAndIsActive("nonexistent", true);
    }

    @Test
    void testFindAllActiveUsers_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByIsActive(true)).thenReturn(users);

        // Act
        List<User> result = userService.findAllActiveUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getUsername(), result.get(0).getUsername());
        verify(userRepository).findByIsActive(true);
    }

    @Test
    void testFindActiveUsersByRole_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByRoleAndIsActive("SALES_PERSON", true)).thenReturn(users);

        // Act
        List<User> result = userService.findActiveUsersByRole("SALES_PERSON");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SALES_PERSON", result.get(0).getRole());
        verify(userRepository).findByRoleAndIsActive("SALES_PERSON", true);
    }

    @Test
    void testFindActiveSalesPersons_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findActiveUsersByRole("SALES_PERSON")).thenReturn(users);

        // Act
        List<User> result = userService.findActiveSalesPersons();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SALES_PERSON", result.get(0).getRole());
        verify(userRepository).findActiveUsersByRole("SALES_PERSON");
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPhone("555-9999");
        updatedUser.setIsActive(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("555-9999", result.getPhone());
        assertFalse(result.getIsActive());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdatePassword_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.updatePassword(1L, "newPassword");

        // Assert
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateLastLoginDate_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.updateLastLoginDate(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deactivateUser(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCountActiveUsersByRole_Success() {
        // Arrange
        when(userRepository.countActiveUsersByRole("SALES_PERSON")).thenReturn(5L);

        // Act
        long count = userService.countActiveUsersByRole("SALES_PERSON");

        // Assert
        assertEquals(5L, count);
        verify(userRepository).countActiveUsersByRole("SALES_PERSON");
    }

    @Test
    void testValidateCredentials_ValidCredentials() {
        // Arrange
        when(userRepository.findByUsernameAndIsActive("testuser", true))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        // Act
        User result = userService.validateCredentials("testuser", "password");

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository).findByUsernameAndIsActive("testuser", true);
        verify(passwordEncoder).matches("password", "hashedPassword");
    }

    @Test
    void testValidateCredentials_InvalidPassword() {
        // Arrange
        when(userRepository.findByUsernameAndIsActive("testuser", true))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        // Act
        User result = userService.validateCredentials("testuser", "wrongpassword");

        // Assert
        assertNull(result);
        verify(userRepository).findByUsernameAndIsActive("testuser", true);
        verify(passwordEncoder).matches("wrongpassword", "hashedPassword");
    }

    @Test
    void testValidateCredentials_UserNotFound() {
        // Arrange
        when(userRepository.findByUsernameAndIsActive("nonexistent", true))
                .thenReturn(Optional.empty());

        // Act
        User result = userService.validateCredentials("nonexistent", "password");

        // Assert
        assertNull(result);
        verify(userRepository).findByUsernameAndIsActive("nonexistent", true);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}

