local key = KEYS[1]
local autoIncr = ARGV[1]
local duration = tonumber(ARGV[2])
local total_requests = tonumber(ARGV[3])
local member = tonumber(ARGV[4])
local current_timestamp_ms = tonumber(ARGV[5])

-- 计算窗口开始的时间戳
local window_start_timestamp = current_timestamp_ms - duration * 1000
local currentCount

-- 移除旧的时间戳
redis.call('ZREMRANGEBYSCORE', key, 0, window_start_timestamp)
currentCount = redis.call('ZCOUNT', key, window_start_timestamp, current_timestamp_ms)

if autoIncr == "true" then
    if currentCount >= total_requests then
        return true
    else
        -- 添加当前时间戳到有序集合
        redis.call('ZADD', key, tostring(current_timestamp_ms), member)
        local expire_time = math.ceil(duration * 1.5)
        -- 设置过期时间
        redis.call('EXPIRE', key, duration)
        return false
    end
else
    -- 检查当前计数是否超过限制
    if currentCount >= total_requests then
        return true
    else
        return false;
    end
end
