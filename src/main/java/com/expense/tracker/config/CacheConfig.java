package com.expense.tracker.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.math.BigDecimal;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_DASHBOARD_SUMMARY   = "dashboard:summary";
    public static final String CACHE_DASHBOARD_BAR_CHART = "dashboard:bar-chart";
    public static final String CACHE_DASHBOARD_PIE_CHART = "dashboard:pie-chart";
    public static final String CACHE_TRANSACTIONS = "transactions";
    public static final String CACHE_CATEGORIES = "categories";
    public static final String CACHE_SYSTEM_CATEGORIES = "system-categories";
    public static final String CACHE_BUDGETS_OVERALL = "budgets:overall";
    public static final String CACHE_BUDGETS_CATEGORY = "budgets:category";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType("com.expense.tracker.")
                                .allowIfSubType("java.util.")
                                .allowIfSubType("java.time.")
                                .allowIfSubType(BigDecimal.class)
                                .build(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                );

        var jsonSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                CACHE_DASHBOARD_SUMMARY,   defaultConfig.entryTtl(Duration.ofMinutes(5)),
                CACHE_DASHBOARD_BAR_CHART, defaultConfig.entryTtl(Duration.ofMinutes(10)),
                CACHE_DASHBOARD_PIE_CHART, defaultConfig.entryTtl(Duration.ofMinutes(5)),
                CACHE_TRANSACTIONS, defaultConfig.entryTtl(Duration.ofMinutes(5)),
                CACHE_CATEGORIES, defaultConfig.entryTtl(Duration.ofMinutes(30)),
                CACHE_SYSTEM_CATEGORIES, defaultConfig.entryTtl(Duration.ofHours(24)),
                CACHE_BUDGETS_OVERALL, defaultConfig.entryTtl(Duration.ofMinutes(10)),
                CACHE_BUDGETS_CATEGORY, defaultConfig.entryTtl(Duration.ofMinutes(10))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .enableStatistics()
                .build();
    }
}
