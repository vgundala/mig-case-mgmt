package com.mig.sales.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT token provider for generating and validating JWT tokens.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long jwtExpiration;
    private final long refreshExpiration;

    public JwtTokenProvider(@Value("${spring.security.jwt.secret}") String jwtSecret,
                          @Value("${spring.security.jwt.expiration}") long jwtExpiration,
                          @Value("${spring.security.jwt.refresh-expiration}") long refreshExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Generate JWT access token.
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername(), jwtExpiration);
    }

    /**
     * Generate JWT refresh token.
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername(), refreshExpiration);
    }

    /**
     * Generate JWT token with custom expiration.
     */
    private String generateToken(String username, long expiration) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Get username from JWT token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }

    /**
     * Get expiration date from JWT token.
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getExpiration();
    }

    /**
     * Check if JWT token is expired.
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Validate JWT token.
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validate JWT token and check if it's not expired.
     */
    public Boolean validateTokenAndNotExpired(String token) {
        return validateToken(token) && !isTokenExpired(token);
    }

    /**
     * Get token expiration time in milliseconds.
     */
    public long getTokenExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Get refresh token expiration time in milliseconds.
     */
    public long getRefreshTokenExpirationTime() {
        return refreshExpiration;
    }
}
