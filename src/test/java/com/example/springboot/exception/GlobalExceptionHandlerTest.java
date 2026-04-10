package com.example.springboot.exception;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import com.example.springboot.config.JwtAuthFilter;
import com.example.springboot.controller.AuthController;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.UserService;

@WebMvcTest(value = AuthController.class, properties = {
    "spring.mvc.throw-exception-if-no-handler-found=true",
    "spring.web.resources.add-mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserService userService;

    @Test
    void handleBadCredentials_ShouldReturn401String() throws Exception {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .with(csrf()) // Bypass CSRF 403
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testUser\", \"password\":\"wrongPassword\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void handleValidationException_ShouldReturnProblemDetailWithErrors() throws Exception {
        // Act & Assert  
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Sending empty body to trigger @Valid
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.title").value("Validation Failed"))
               .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void handleNoHandlerFound_ShouldReturn404ProblemDetail() throws Exception {
        mockMvc.perform(get("/api/does-not-exist"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.title").value("API Not Found"));
    }
}
