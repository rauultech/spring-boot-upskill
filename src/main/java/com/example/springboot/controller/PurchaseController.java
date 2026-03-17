package com.example.springboot.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.PurchaseRequest;
import com.example.springboot.services.PurchaseService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    
    @PostMapping("/buy")
    public PurchaseRequest buyProduct(@RequestBody PurchaseRequest request) {
        return purchaseService.buyProduct(request.getUserId(), request.getProductId());
    }
    
    @GetMapping("/list")
    public Page<PurchaseRequest> getPurchaseList(Pageable pageable) {
        return purchaseService.getPurchaseList(pageable);
    }
    
}
