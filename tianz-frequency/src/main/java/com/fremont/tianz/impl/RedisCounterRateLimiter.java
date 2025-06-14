package com.fremont.tianz.impl;

import com.fremont.tianz.AbstractRedisRateLimiter;
import com.fremont.tianz.FrequencyConfig;
import com.fremont.tianz.RedisLimiterTypeEnum;
import com.fremont.tianz.gateway.RedisServiceGateway;
import com.fremont.tianz.gateway.RedisServiceGatewayImpl;
import com.fremont.tianz.utils.LuaIoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Redis计数器限流器实现类
 * @author: tianzfm
 * @create: 2025-06-14 10:39
 **/
@Slf4j
public class RedisCounterRateLimiter extends AbstractRedisRateLimiter {
    private RedisServiceGateway redisServiceGateway;

//    private boolean scriptLoaded = false;

    public RedisCounterRateLimiter(RedisServiceGateway redisServiceGateway, FrequencyConfig frequencyConfig) {
        this.rateLimiterType = RedisLimiterTypeEnum.REDIS_COUNTER.getType();
        this.frequencyConfig = frequencyConfig;
        this.redisServiceGateway = redisServiceGateway;
        luaScriptLoaded();
    }


    @Override
    protected boolean doRateLimited(boolean autoIncr, String redisKey) {
        DefaultRedisScript<Boolean> redisScript = getRedisScript();
        if (Objects.isNull(redisScript)) {
            return false;
        }
        List<String> keys = new ArrayList<>();
        keys.add(redisKey);
        return Boolean.TRUE.equals(redisServiceGateway.execute(redisScript,
                keys,
                autoIncr ? "true" : "false",
                String.valueOf(frequencyConfig.getDuration()),
                String.valueOf(frequencyConfig.getTotalRequests()
                )));
    }

    @Override
    protected String getLuaPath() {
        return "redis_counter_rate_limit.lua";
    }

    @Override
    protected String getRateLimiterType() {
        return RedisLimiterTypeEnum.REDIS_COUNTER.getType();
    }

    private DefaultRedisScript<Boolean> getRedisScript() {
        String luaScript = LuaIoUtils.readLua(getLuaPath());
        if (StringUtils.isEmpty(luaScript)) {
            return null;
        }
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(luaScript);
        return redisScript;
    }

}