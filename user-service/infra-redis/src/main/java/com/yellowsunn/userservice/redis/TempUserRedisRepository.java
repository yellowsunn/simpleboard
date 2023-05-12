package com.yellowsunn.userservice.redis;

import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class TempUserRedisRepository implements TempUserCacheRepository {
    private final RedisTemplate<String, TempUser> redisTemplate;
    private static final String PREFIX_KEY = "temp-user-";

    public TempUserRedisRepository(RedisTemplate<String, TempUser> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(TempUser tempUser, Duration timeout) {
        String key = generateKey(tempUser.getToken());
        redisTemplate.opsForValue().set(key, tempUser, timeout);
    }

    @Override
    public TempUser findByToken(String token) {
        String key = generateKey(token);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean deleteByToken(String token) {
        String key = generateKey(token);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    private String generateKey(String token) {
        return PREFIX_KEY + token;
    }
}
