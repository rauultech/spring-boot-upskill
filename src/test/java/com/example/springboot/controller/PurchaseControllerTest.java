package com.example.springboot.controller;

import com.example.springboot.config.SecurityConfig;
import com.example.springboot.dto.PurchaseRequest;
import com.example.springboot.services.CustomUserDetailsService;
import com.example.springboot.services.JwtService;
import com.example.springboot.services.PurchaseService;
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

@WebMvcTest(PurchaseController.class)
@Import(SecurityConfig.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void buyProduct_withValidRequest_returnsPurchaseRequest() throws Exception {
        // 1. Arrange
        PurchaseRequest request = new PurchaseRequest();
        request.setUserId(1L);
        request.setProductId(10L);

        Mockito.when(purchaseService.buyProduct(1L, 10L)).thenReturn(request);

        // 2. Act & Assert
        mockMvc.perform(post("/purchase/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.productId").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getPurchaseList_whenRequested_returnsPagedPurchases() throws Exception {
        // 1. Arrange
        Page<PurchaseRequest> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(purchaseService.getPurchaseList(any())).thenReturn(page);

        // 2. Act & Assert
        mockMvc.perform(get("/purchase/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}