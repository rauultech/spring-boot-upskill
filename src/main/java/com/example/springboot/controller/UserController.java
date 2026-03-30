package com.example.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.entity.User;
import com.example.springboot.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/add")
    public User save(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/list")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-data")
    public String getAdminData() {
        return "Only admin can see this";
    }
    
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user-data")
    public String getUserData() {
        return "User / Admin data";
    }
}
