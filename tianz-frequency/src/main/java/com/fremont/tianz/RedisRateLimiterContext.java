package com.fremont.tianz;

import com.fremont.tianz.gateway.RedisServiceGateway;
import com.fremont.tianz.impl.RedisCounterRateLimiter;
import com.fremont.tianz.impl.RedisSlidingWindowRateLimiter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis限流器上下文管理类
 * @author: tianzfm
 * @create: 2025-06-14 17:02
 **/
@Component
public class RedisRateLimiterContext {
    @Resource
    private RedisServiceGateway redisServiceGateway;

    private final ConcurrentHashMap<String, AbstractRedisRateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    public RedisRateLimiter getMatchedRateLimiter(RedisLimiterTypeEnum redisLimiterTypeEnum, FrequencyConfig frequencyConfig) {
        if (Objects.isNull(frequencyConfig)) {
            return null;
        }
        String key = getRateLimitKey(redisLimiterTypeEnum, frequencyConfig);
        return rateLimiterMap.computeIfAbsent(key, k -> createRateLimiter(redisLimiterTypeEnum, frequencyConfig));
    }

    private AbstractRedisRateLimiter createRateLimiter(RedisLimiterTypeEnum redisLimiterTypeEnum, FrequencyConfig frequencyConfig) {
        switch (redisLimiterTypeEnum) {
            case REDIS_COUNTER:
                return new RedisCounterRateLimiter(redisServiceGateway, frequencyConfig);
            case REDIS_SLIDING_WINDOW:
                return new RedisSlidingWindowRateLimiter(redisServiceGateway, frequencyConfig);
            default:
                throw new IllegalArgumentException("Unsupported Redis Limiter Type: " + redisLimiterTypeEnum.getType());
        }
    }

    private String getRateLimitKey(RedisLimiterTypeEnum redisLimiterTypeEnum, FrequencyConfig frequencyConfig) {
        return redisLimiterTypeEnum.getType() + "_" + frequencyConfig.toString();
    }
}