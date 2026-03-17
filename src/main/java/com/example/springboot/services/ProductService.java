package com.example.springboot.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream().toList();
    }   
}
