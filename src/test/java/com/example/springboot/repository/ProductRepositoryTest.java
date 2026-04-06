package com.example.springboot.repository;

import com.example.springboot.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByName_whenProductExists_returnsList() {
        // Arrange
        Product product = new Product();
        product.setName("Gaming Laptop");
        // Add other fields if your Entity requires them (price, category, etc.)
        
        entityManager.persistAndFlush(product);

        // Act
        List<Product> foundProducts = productRepository.findByName("Gaming Laptop");

        // Assert
        assertFalse(foundProducts.isEmpty());
        assertEquals(1, foundProducts.size());
        assertEquals("Gaming Laptop", foundProducts.get(0).getName());
    }

    @Test
    void findByName_whenMultipleProductsHaveSameName_returnsAll() {
        // Arrange
        Product p1 = new Product();
        p1.setName("USB Cable");
        Product p2 = new Product();
        p2.setName("USB Cable");

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();

        // Act
        List<Product> results = productRepository.findByName("USB Cable");

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    void findByName_whenProductDoesNotExist_returnsEmptyList() {
        // Act
        List<Product> results = productRepository.findByName("Invisible Ink");

        // Assert
        assertTrue(results.isEmpty());
    }
}