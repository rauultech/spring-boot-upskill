package com.example.springboot.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

import com.example.springboot.config.SecurityConfig;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;


    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    // ---------------------

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getUserById_whenUserExists_returnsUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setName("John Doe");
        user.setRole(Role.USER);
        
        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.role").value("USER"))
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAdminData_whenUserIsRegularUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/user/admin-data"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAdminData_whenUserIsAdmin_returnsSuccessMessage() throws Exception {
        mockMvc.perform(get("/user/admin-data"))
                .andExpect(status().isOk())
                .andExpect(content().string("Only admin can see this"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllUsers_whenRequested_returnsPagedUsers() throws Exception {
        // 1. Arrange
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(userService.getUsers(any())).thenReturn(emptyPage);

        // 2. Act & Assert
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
