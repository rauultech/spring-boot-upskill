package com.example.springboot.dto;

public record UserDto(
    Long id,
    String name,
    String username,
    String info
) {}
