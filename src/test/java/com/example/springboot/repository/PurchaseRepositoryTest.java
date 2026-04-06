package com.example.springboot.repository;

import com.example.springboot.entity.Product;
import com.example.springboot.entity.Purchase;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User savedUser;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        // Arrange
        User user = new User();
        user.setUsername("buyer123");
        user.setName("Buyer");
        user.setPassword("pass");
        user.setRole(Role.USER);
        savedUser = entityManager.persistAndFlush(user);

        Product product = new Product();
        product.setName("Mechanical Keyboard");
        savedProduct = entityManager.persistAndFlush(product);
    }

    @Test
    void findByUserId_whenPurchasesExist_returnsList() {
        // Arrange
        Purchase purchase = new Purchase();
        purchase.setUser(savedUser);
        purchase.setProduct(savedProduct);
        entityManager.persistAndFlush(purchase);

        // Act
        List<Purchase> results = purchaseRepository.findByUserId(savedUser.getId());

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("buyer123", results.get(0).getUser().getUsername());
    }

    @Test
    void findByProductId_whenPurchasesExist_returnsList() {
        // Arrange
        Purchase purchase = new Purchase();
        purchase.setUser(savedUser);
        purchase.setProduct(savedProduct);
        entityManager.persistAndFlush(purchase);

        // Act
        List<Purchase> results = purchaseRepository.findByProductId(savedProduct.getId());

        // Assert
        assertEquals(1, results.size());
        assertEquals("Mechanical Keyboard", results.get(0).getProduct().getName());
    }

    @Test
    void findByUserId_whenNoPurchases_returnsEmptyList() {
        // Act
        List<Purchase> results = purchaseRepository.findByUserId(999L);

        // Assert
        assertTrue(results.isEmpty());
    }
}