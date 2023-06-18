local viewCount = tonumber(redis.call('HGET', KEYS[1], KEYS[2]))
local deleteCount = tonumber(redis.call('HDEL', KEYS[1], KEYS[2]))

if viewCount ~= nil and deleteCount ~= nil and deleteCount > 0
then
  return viewCount
end
return 0
