package com.peakform.secure;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {

    private static final int MAX_REQUESTS_PER_WINDOW = 10;
    private static final long WINDOW_SECONDS = 60;

    private static class Window {
        AtomicInteger count = new AtomicInteger(0);
        Instant windowStart = Instant.now();
    }

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public boolean allowRequest(String sessionId) {
        Window window = windows.computeIfAbsent(sessionId, k -> new Window());
        synchronized (window) {
            if (Instant.now().getEpochSecond() - window.windowStart.getEpochSecond() > WINDOW_SECONDS) {
                window.windowStart = Instant.now();
                window.count.set(0);
            }
            return window.count.incrementAndGet() <= MAX_REQUESTS_PER_WINDOW;
        }
    }
}
