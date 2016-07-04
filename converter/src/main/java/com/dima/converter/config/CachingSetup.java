package com.dima.converter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class CachingSetup implements JCacheManagerCustomizer
{
    @Value("${live.rates.ttl.minutes}")
    private long ttlMinutes;

    @Override
    public void customize(CacheManager cacheManager)
    {
        cacheManager.createCache("liveRates", new MutableConfiguration<>()
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(MINUTES,ttlMinutes)))
                .setStoreByValue(false)
                .setStatisticsEnabled(true));

        cacheManager.createCache("historicalRates", new MutableConfiguration<>()
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(Duration.ETERNAL))
                .setStoreByValue(false)
                .setStatisticsEnabled(true));
    }
}