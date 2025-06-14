local key = KEYS[1]
local autoIncr = ARGV[1]
local expireTime = tonumber(ARGV[2])
local totalRequests = tonumber(ARGV[3])
local currentCount


if autoIncr == "true" then
    -- Increment the counter and set the expiration time
    currentCount = redis.call("INCR", key)
    if currentCount == 1 then
        redis.call("EXPIRE", key, expireTime)
    end
else
    -- Get the current count without incrementing
    local count = redis.call("GET", key)
    if count == false then
        return false
    end
    -- 安全转换：非数字值视为0
    local currentCount = tonumber(count) or 0
end

if currentCount >= totalRequests then
    return true
else
    return false
end