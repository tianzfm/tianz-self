package com.fremont.tianz;

public interface RedisRateLimiter {
    /**
     * 频控接口
     *
     * @param autoIncr 是否自增
     * @param redisKey redis键
     * @return 频控命中（true） 频控未命中（false）
     */
    boolean isRateLimited(boolean autoIncr, String redisKey);
}
