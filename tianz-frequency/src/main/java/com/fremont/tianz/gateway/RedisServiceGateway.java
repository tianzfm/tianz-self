package com.fremont.tianz.gateway;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

/**
 * @author: tianzfm
 * @create: 2025-06-14 11:39
 **/
public interface RedisServiceGateway {
    <T> T execute(RedisScript<T> script, List<String> keys, Object... args);
    <T> T execute(boolean ignoreException, RedisScript<T> script, List<String> keys, Object... args);
    String scriptLoaded(byte[] script);
    String scriptLoaded(boolean ignoreException, byte[] script);
}