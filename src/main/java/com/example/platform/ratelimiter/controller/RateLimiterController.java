package com.example.platform.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.platform.ratelimiter.service.RateLimiterService;

@RestController
@RequestMapping("/rate-limit")
public class RateLimiterController {
    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/check/{clientId}")
    public String check(@PathVariable String clientId) {
        boolean allowed = rateLimiterService.isRequestAllowed(clientId);
        return allowed ? "ALLOWED" : "BLOCKED";
    }
}
