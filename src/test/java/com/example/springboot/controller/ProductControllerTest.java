package com.example.springboot.controller;

import com.example.springboot.config.SecurityConfig;
import com.example.springboot.dto.ProductRequest;
import com.example.springboot.entity.Product;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void saveProduct_withValidRequest_returnsProduct() throws Exception {
        // 1. Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        Product savedProduct = new Product(1L, "Laptop");

        Mockito.when(productService.saveProduct(any(ProductRequest.class))).thenReturn(savedProduct);

        // 2. Act & Assert
        mockMvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void saveProduct_whenUserNotAuthenticated_returnsUnauthorized() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");

        mockMvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getProduct_whenProductExists_returnsProduct() throws Exception {
        // 1. Arrange
        Product product = new Product(99L, "Phone");
        Mockito.when(productService.getProductById(99L)).thenReturn(product);

        // 2. Act & Assert
        mockMvc.perform(get("/product/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllProducts_whenRequested_returnsPagedProducts() throws Exception {
        // 1. Arrange
        Page<Product> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(productService.getAllProducts(any())).thenReturn(page);

        // 2. Act & Assert
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}