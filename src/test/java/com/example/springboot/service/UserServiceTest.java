package com.example.springboot.service;

import com.example.springboot.dto.CreateUserRequest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_withValidRequest_returnsUserDto() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "johndoe", "pass123", Role.USER);
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John");
        savedUser.setUsername("johndoe");

        Mockito.when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.createUser(request);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.username());
        assertEquals(1L, result.id());
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void createUser_withDuplicateUsername_throwsRuntimeException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "johndoe", "pass", Role.USER);
        Mockito.when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(request);
        });

        assertEquals("Username already exists", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void getUserById_whenUserExists_returnsUser() {
        // Arrange
        User user = new User();
        user.setId(10L);
        Mockito.when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void getUserById_whenUserDoesNotExist_returnsNull() {
        // Arrange
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(99L);

        // Assert
        assertNull(result);
    }
}