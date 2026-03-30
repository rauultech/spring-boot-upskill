package com.example.springboot.dto;

import com.example.springboot.entity.Role;

public record CreateUserRequest(
    String name,
    String username,
    String password,
    Role role
) {}