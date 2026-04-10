package com.example.springboot.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dummy controller for testing
 */
@RestController
class TestController {

        @GetMapping("/auth/login")
        public String login() {
            return "ok";
        }

        @GetMapping("/api/test")
        public String secured() {
            return "secured";
        }

        @GetMapping("/actuator/health")
        public String health() {
            return "UP";
        }
    }