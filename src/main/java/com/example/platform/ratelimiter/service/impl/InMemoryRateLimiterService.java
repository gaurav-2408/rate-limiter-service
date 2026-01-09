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
    public boolean isRequestAllowed(String clientId) {
        long now = System.currentTimeMillis();

        windowStart.putIfAbsent(clientId, now);
        counters.putIfAbsent(clientId, new AtomicInteger(0));

        if (now - windowStart.get(clientId) > WINDOW_MS) {
            windowStart.put(clientId, now);
            counters.get(clientId).set(0);
        }

        return counters.get(clientId).incrementAndGet() <= LIMIT;
    }

}
