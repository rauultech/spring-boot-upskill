package com.example.springboot.dto;

import com.example.springboot.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    String name,
    @NotBlank(message = "Username is required")
    String username,
    @NotBlank(message = "Password is required")
    String password,
    @NotNull(message = "Role is required")
    Role role
) {}