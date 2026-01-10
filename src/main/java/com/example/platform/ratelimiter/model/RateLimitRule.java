package com.example.platform.ratelimiter.model;

import lombok.Data;

@Data
public class RateLimitRule {
    private String clientId;
    private int limit;
    private int windowInSeconds;
    private String api;
    private String httpMethod;
}
