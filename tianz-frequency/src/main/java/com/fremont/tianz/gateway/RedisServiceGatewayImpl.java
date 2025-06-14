package com.fremont.tianz.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: tianzfm
 * @create: 2025-06-14 10:52
 **/
@Slf4j
@Service
public class RedisServiceGatewayImpl implements RedisServiceGateway {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return this.execute(Boolean.TRUE, script, keys, args);
    }

    @Override
    public <T> T execute(boolean ignoreException, RedisScript<T> script, List<String> keys, Object... args) {
        try {
            return stringRedisTemplate.execute(script, keys, args);
        } catch (Exception e) {
            log.error("Redis script execution failed", e);
            if (!ignoreException) {
                throw e;
            }
            return null;
        }
    }

    @Override
    public String scriptLoaded(byte[] script) {
        return this.scriptLoaded(Boolean.TRUE, script);
    }

    @Override
    public String scriptLoaded(boolean ignoreException, byte[] script) {
        try {
            return stringRedisTemplate.getConnectionFactory().getConnection().scriptLoad(script);
        } catch (Exception e) {
            log.error("Redis scriptLoaded failed", e);
            if (!ignoreException) {
                throw e;
            }
            return null;
        }
    }
}