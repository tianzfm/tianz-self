package com.fremont.tianz;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis限流类型枚举类
 * 该类用于定义不同的Redis限流类型，便于在实现类中进行区分和处理。
 *
 * @author: tianzfm
 * @create: 2025-06-14 10:40
 **/
@Getter
@Slf4j
public enum RedisLimiterTypeEnum {
    REDIS_SLIDING_WINDOW("redisSlidingWindow", "Redis滑动窗口限流"),

    REDIS_COUNTER("redisCounter", "Redis计数器限流"),
    ;

    final String type;
    final String description;

    RedisLimiterTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public static RedisLimiterTypeEnum fromType(String type) {
        for (RedisLimiterTypeEnum limiterType : values()) {
            if (limiterType.getType().equals(type)) {
                return limiterType;
            }
        }
        log.warn("Unknown RedisLimiterTypeEnum type: {}", type);
        // 或者抛出异常，根据实际需求决定
        return null;
    }

}