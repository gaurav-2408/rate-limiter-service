package com.example.platform.ratelimiter.service;

public interface RateLimiterService {
    boolean isRequestAllowed(String clientId, String api, String httpMethod);    
} 