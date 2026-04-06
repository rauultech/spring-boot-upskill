package com.example.springboot.service;

import com.example.springboot.dto.ProductRequest;
import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.services.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void saveProduct_withValidRequest_returnsSavedProduct() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Smartphone");
        
        Product savedProduct = new Product();
        savedProduct.setId(101L);
        savedProduct.setName("Smartphone");

        Mockito.when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.saveProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Smartphone", result.getName());
        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_whenProductExists_returnsProduct() {
        // Arrange
        Product product = new Product();
        product.setId(5L);
        product.setName("Monitor");
        
        Mockito.when(productRepository.findById(5L)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(5L);

        // Assert
        assertNotNull(result);
        assertEquals("Monitor", result.getName());
    }

    @Test
    void getProductById_whenProductDoesNotExist_returnsNull() {
        // Arrange
        Mockito.when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void getAllProducts_whenRequested_returnsPagedProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPage = new PageImpl<>(Collections.emptyList());
        
        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act
        Page<Product> result = productService.getAllProducts(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        Mockito.verify(productRepository).findAll(pageable);
    }
}