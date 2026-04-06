package com.example.springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springboot.services.JwtService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_withUsername_returnsValidToken() {
        // Act
        String token = jwtService.generateToken("testuser");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_withValidToken_returnsCorrectUsername() {
        // Arrange
        String token = jwtService.generateToken("johndoe");

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("johndoe", username);
    }

    @Test
    void isTokenValid_withCorrectUser_returnsTrue() {
        // Arrange
        String username = "testuser";
        String token = jwtService.generateToken(username);
        
        Mockito.when(userDetails.getUsername()).thenReturn(username);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_withIncorrectUser_returnsFalse() {
        // Arrange
        String token = jwtService.generateToken("realuser");
        Mockito.when(userDetails.getUsername()).thenReturn("imposter");

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_withFreshToken_returnsFalse() {
        String token = jwtService.generateToken("testuser");
        
        Mockito.when(userDetails.getUsername()).thenReturn("testuser");
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}