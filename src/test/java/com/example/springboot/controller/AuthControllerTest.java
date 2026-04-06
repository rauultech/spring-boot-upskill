package com.example.springboot.controller;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

import com.example.springboot.controller.AuthController;
import com.example.springboot.dto.CreateUserRequest;
import com.example.springboot.dto.LoginRequest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.Role;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @Test
    void login_withValidCredentials_returnsJwtToken() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "password123");
        UserDetails userDetails = new User("testUser", "password123", new ArrayList<>());

        Mockito.when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken("testUser")).thenReturn("mocked-jwt-token");

        // Act and Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    void login_withInvalidCredentials_returnsUnauthorized() throws Exception {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest("wronguser", "wrongpass");

        Mockito.doThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"))
               .when(authenticationManager)
               .authenticate(any(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized()); 
    }

    @Test
    void create_withValidRequest_returnsUserDto() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John Doe", "john.doe@example.com", "password123", Role.USER);

        UserDto expectedUserDto = new UserDto(1L, "John Doe", "john.doe@example.com", "User created successfully");

        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(expectedUserDto);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("john.doe@example.com"))
                .andExpect(jsonPath("$.info").value("User created successfully"));
    }

    @Test
    void create_withNullName_returnsBadRequest() throws Exception {
        // Testing name is missing
        CreateUserRequest request = new CreateUserRequest(null, "johndoe", "pass123", Role.USER);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withShortPassword_returnsBadRequest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("John", "johndoe", "", Role.USER);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withNullRole_returnsBadRequest() throws Exception {
        // Testing role is mandatory
        CreateUserRequest request = new CreateUserRequest("John", "johndoe", "pass123", null);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
