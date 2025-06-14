package com.fremont.tianz.impl;

import com.fremont.tianz.AbstractRedisRateLimiter;
import com.fremont.tianz.FrequencyConfig;
import com.fremont.tianz.RedisLimiterTypeEnum;
import com.fremont.tianz.gateway.RedisServiceGateway;
import com.fremont.tianz.utils.LuaIoUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Redis滑动窗口限流器实现类
 * @author: tianzfm
 * @create: 2025-06-14 16:47
 **/
public class RedisSlidingWindowRateLimiter extends AbstractRedisRateLimiter {


    public RedisSlidingWindowRateLimiter(RedisServiceGateway redisServiceGateway, FrequencyConfig frequencyConfig) {
        this.frequencyConfig = frequencyConfig;
        this.redisServiceGateway = redisServiceGateway;
        luaScriptLoaded();
    }

    @Override
    protected boolean doRateLimited(boolean autoIncr, String redisKey) {
        long currentTimeMillis = System.currentTimeMillis();
        Random random = new Random();
        int randomNum = 1000 + random.nextInt(9000); // 生成1000到9999之间的随机数
        DefaultRedisScript<Boolean> redisScript = getRedisScript();
        if (Objects.isNull(redisScript)) {
            return false;
        }
        ArrayList<String> keys = new ArrayList<>();
        keys.add(redisKey);
        ArrayList<Object> args = new ArrayList<>();
        args.add(autoIncr ? "true" : "false");
        args.add(String.valueOf(frequencyConfig.getDuration()));
        args.add(String.valueOf(frequencyConfig.getTotalRequests()));
        args.add(currentTimeMillis + randomNum); // 添加随机数以避免冲突
        args.add(String.valueOf(currentTimeMillis));

        return Boolean.TRUE.equals(redisServiceGateway.execute(redisScript, keys, args.toArray()));
    }

    @Override
    protected String getRateLimiterType() {
        return RedisLimiterTypeEnum.REDIS_SLIDING_WINDOW.getType();
    }

    @Override
    protected String getLuaPath() {
        return "redis_sliding_window_rate_limit.lua";
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