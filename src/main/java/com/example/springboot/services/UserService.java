package com.example.springboot.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springboot.dto.CreateUserRequest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserDto createUser(CreateUserRequest request) {
        userRepository.findByUsername(request.username())
        .ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });

        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role() != null ? request.role() : Role.USER);
        User savedUser = userRepository.save(user);
        return new UserDto(
            String.valueOf(savedUser.getId()),
            savedUser.getName(),
            savedUser.getUsername(),
            "Additional info can go here"
        );
    }
}
