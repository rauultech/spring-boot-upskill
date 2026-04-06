package com.example.springboot.repository;

import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void findByUsername_whenUserExists_returnsUser() {
        // Arrange
        User user = new User();
        user.setUsername("bob_builder");
        user.setName("Bob");
        user.setPassword("secret");
        user.setRole(Role.USER);
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> found = userRepository.findByUsername("bob_builder");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getName());
    }

    @Test
    void findByName_whenMultipleUsersHaveSameName_returnsList() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setName("John Doe");
        user1.setPassword("pass");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setName("John Doe"); // Same name
        user2.setPassword("pass");
        user2.setRole(Role.USER);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        List<User> results = userRepository.findByName("John Doe");

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    void findByUsername_whenUserDoesNotExist_returnsEmpty() {
        // Act
        Optional<User> found = userRepository.findByUsername("non_existent");

        // Assert
        assertFalse(found.isPresent());
    }
}