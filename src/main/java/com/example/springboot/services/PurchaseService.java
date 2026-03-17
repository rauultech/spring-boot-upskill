package com.example.springboot.services;

import org.springframework.stereotype.Service;

import com.example.springboot.dto.PurchaseRequest;
import com.example.springboot.entity.Product;
import com.example.springboot.entity.Purchase;
import com.example.springboot.entity.User;
import com.example.springboot.repository.PurchaseRepository;

@Service
public class PurchaseService {
    private final UserService userService;
    private final ProductService productService;
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(UserService userService, ProductService productService, PurchaseRepository purchaseRepository) {
        this.userService = userService;
        this.productService = productService;
        this.purchaseRepository = purchaseRepository;
    }

    public PurchaseRequest buyProduct(long userId, long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        } 
        
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setProduct(product);
        purchase.setPurchaseDate(java.time.LocalDate.now());

        PurchaseRequest response = new PurchaseRequest();
        response.setUserId(userId);
        response.setProductId(productId);
        purchaseRepository.save(purchase);

        return response;
    }
}
