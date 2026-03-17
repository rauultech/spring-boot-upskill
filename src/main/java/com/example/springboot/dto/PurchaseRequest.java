package com.example.springboot.dto;

public class PurchaseRequest {
    private Long userId;
    private Long productId;

    public PurchaseRequest() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
}
