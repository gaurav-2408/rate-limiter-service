package com.example.platform.ratelimiter.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.example.platform.ratelimiter.model.RateLimitRule;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {
    //Bind external configuration (application.yaml) into Java objects.
    private List<RateLimitRule>rules;
}
