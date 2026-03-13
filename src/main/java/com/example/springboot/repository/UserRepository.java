package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.springboot.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
}