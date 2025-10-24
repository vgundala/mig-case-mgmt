package com.mig.sales.api.controller;

import com.mig.sales.api.model.dto.LoginRequest;
import com.mig.sales.api.model.dto.LoginResponse;
import com.mig.sales.api.model.dto.RegisterRequest;
import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.mapper.UserMapper;
import com.mig.sales.api.repository.UserRepository;
import com.mig.sales.api.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserDTO userDTO = userMapper.toDTO(user);
        
        LoginResponse response = LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(tokenProvider.getTokenExpirationTime() / 1000) // Convert to seconds
            .user(userDTO)
            .build();
        
        log.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration attempt for user: {}", registerRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Username already exists: {}", registerRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // Check if email already exists
        if (registerRequest.getEmail() != null && userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Email already exists: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        User user = User.builder()
            .username(registerRequest.getUsername())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .email(registerRequest.getEmail())
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .role(registerRequest.getRole())
            .isActive(true)
            .build();
        
        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.toDTO(savedUser);
        
        log.info("User registered successfully: {}", savedUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT access token using refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<LoginResponse> refresh(@RequestParam String refreshToken) {
        log.info("Token refresh attempt");
        
        if (!tokenProvider.validateTokenAndNotExpired(refreshToken)) {
            log.warn("Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create new authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            username, null, user.getAuthorities()
        );
        
        String newAccessToken = tokenProvider.generateAccessToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);
        
        UserDTO userDTO = userMapper.toDTO(user);
        
        LoginResponse response = LoginResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(tokenProvider.getTokenExpirationTime() / 1000)
            .user(userDTO)
            .build();
        
        log.info("Token refreshed successfully for user: {}", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user (client should discard tokens)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public ResponseEntity<Void> logout() {
        log.info("User logout");
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
