package com.fremont.tianz;

import com.fremont.tianz.gateway.RedisServiceGateway;
import com.fremont.tianz.utils.LuaIoUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

public abstract class AbstractRedisRateLimiter implements RedisRateLimiter {

    protected String rateLimiterType;

    protected FrequencyConfig frequencyConfig;

    protected RedisServiceGateway redisServiceGateway;


    @Override
    public boolean isRateLimited(boolean autoIncr, String redisKey) {
        return doRateLimited(autoIncr, redisKey);
    }

    protected abstract boolean doRateLimited(boolean autoIncr, String redisKey);

    protected abstract String getRateLimiterType();
    protected abstract String getLuaPath();


    private DefaultRedisScript<Boolean> getRedisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText("return redis.call('EVAL', script, keys, args)");
        return redisScript;
    }

    public String luaScriptLoaded() {
        String luaPath = getLuaPath();
        if (StringUtils.isEmpty(luaPath)) {
            return null;
        }
        String luaScript = LuaIoUtils.readLua(luaPath);
        return redisServiceGateway.scriptLoaded(luaScript.getBytes());
    }
}
