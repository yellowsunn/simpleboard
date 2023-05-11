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
        String key = generateKey(tempUser.getEmail());
        redisTemplate.opsForValue().set(key, tempUser, timeout);
    }

    @Override
    public TempUser findByEmail(String email) {
        String key = generateKey(email);
        return redisTemplate.opsForValue().get(key);
    }

    private String generateKey(String email) {
        return PREFIX_KEY + email;
    }
}
