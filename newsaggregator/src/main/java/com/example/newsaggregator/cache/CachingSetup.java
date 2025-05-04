package com.example.newsaggregator.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import jakarta.transaction.Transactional;

public class CachingSetup {

private final CacheManager cacheManager;
	
	private final ReentrantLock cacheClearLock = new ReentrantLock();
	
	public CachingSetup(CacheManager cacheManager)
	{
		this.cacheManager = cacheManager;
		
	}
	
	private static final Logger logInfo = LoggerFactory.getLogger(CachingSetup.class);
	@Scheduled(fixedRate = 9000, initialDelay = 1500) // You can later make this configurable
    @Transactional
    @Async
    public void clearAllCaches() {
        boolean acquiredLock = false;
        List<String> clearedCaches = new ArrayList<>();

        try {
            acquiredLock = cacheClearLock.tryLock(5, TimeUnit.SECONDS); // waits up to 5 seconds to acquire lock

            if (acquiredLock) {
                cacheManager.getCacheNames().forEach(cacheName -> {
                    Cache cache = cacheManager.getCache(cacheName);
                    if (cache != null) {
                        cache.clear();
                        clearedCaches.add(cacheName);
                    }
                });

                if (!clearedCaches.isEmpty()) {
                    logInfo.info("Cleared the following caches: {}", String.join(", ", clearedCaches));
                } else {
                    logInfo.info("No caches to clear.");
                }
            } else {
                logInfo.info("Another cache clear operation is already in progress. Skipping this run.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt status
            logInfo.error("Cache clearing operation was interrupted.", e);
        } finally {
            if (acquiredLock) {
                cacheClearLock.unlock();
            }
        }
    }
}
