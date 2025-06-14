package com.fremont.tianz;

import org.junit.Test;

/**
 * @author: tianzfm
 * @create: 2025-06-14 17:09
 **/
public class FrequencyTest {

    @Test
    public void testRedisRateLimiter() {
        RedisRateLimiterContext redisRateLimiterContext = new RedisRateLimiterContext();
        redisRateLimiterContext.getMatchedRateLimiter(RedisLimiterTypeEnum.REDIS_COUNTER,
                new FrequencyConfig().setDuration(60 * 1000).setTotalRequests(100));

        boolean isLimited = redisRateLimiterContext.getMatchedRateLimiter(RedisLimiterTypeEnum.REDIS_COUNTER,
                new FrequencyConfig().setDuration(60 * 1000).setTotalRequests(100))
                .isRateLimited(Boolean.TRUE, "frequency:agent:${clientID}:${agentId}");
    }
}