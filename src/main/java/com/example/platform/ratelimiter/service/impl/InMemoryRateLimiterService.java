package com.example.platform.ratelimiter.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.platform.ratelimiter.service.RateLimiterService;

@Service
public class InMemoryRateLimiterService implements RateLimiterService {

    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 60_000;

    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStart = new ConcurrentHashMap<>();

    @Override
    public boolean isRequestAllowed(String clientId, String api, String httpMethod) {
        long now = System.currentTimeMillis();
        String key = buildKey(clientId, api, httpMethod);

        windowStart.putIfAbsent(key, now);
        counters.putIfAbsent(key, new AtomicInteger(0));

        if (now - windowStart.get(key) > WINDOW_MS) {
            windowStart.put(key, now);
            counters.get(key).set(0);
        }

        return counters.get(key).incrementAndGet() <= LIMIT;
    }

    private String buildKey(String clientId, String api, String httpMethod) {
        return clientId + "|" + api + "|" + httpMethod;
    }
}
