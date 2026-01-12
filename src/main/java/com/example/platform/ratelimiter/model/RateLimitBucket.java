package com.example.platform.ratelimiter.model;

import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitBucket {
    /*
     * 1. If behavior operates on the object’s own state, it belongs in the model.
     * 2. This is a DOMAIN MODEL and NOT SERVICE
     * 
     */
    private final AtomicInteger counter = new AtomicInteger(0);
    private long windowStart;
    private final int limit;
    private final long windowMs;

    public RateLimitBucket(int limit, long windowMs) {
        this.limit = limit;
        this.windowMs = windowMs;
        this.windowStart = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        if (now - windowStart > windowMs) {
            windowStart = now;
            counter.set(0);
        }

        return counter.incrementAndGet() <= limit;
    }
}
