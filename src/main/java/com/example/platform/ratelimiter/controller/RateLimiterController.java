package com.example.platform.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.platform.ratelimiter.service.RateLimiterService;

@RestController
@RequestMapping("/rate-limit")
public class RateLimiterController {
    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/check")
    public String check(
        @RequestHeader("X-Client-Id") String clientId,
        @RequestParam String api,
        @RequestParam(name = "httpMethod") String httpMethod

    ) {
        boolean allowed = rateLimiterService.isRequestAllowed(clientId, api, httpMethod);
        return allowed ? "ALLOWED" : "BLOCKED";
    }
}
