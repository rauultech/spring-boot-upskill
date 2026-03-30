package com.example.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.CreateUserRequest;
import com.example.springboot.dto.LoginRequest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());

        return jwtService.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public UserDto create(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
    
}
