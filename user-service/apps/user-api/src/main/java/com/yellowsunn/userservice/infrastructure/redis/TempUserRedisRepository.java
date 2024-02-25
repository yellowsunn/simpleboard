package com.yellowsunn.userservice.infrastructure.redis;

import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.application.port.TempUserCacheRepository;
import java.time.Duration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    public TempUser findByTokenAndCsrfToken(String token, String csrfToken) {
        String key = generateKey(token);
        TempUser tempUser = redisTemplate.opsForValue().get(key);
        if (tempUser != null && StringUtils.equals(tempUser.getCsrfToken(), csrfToken)) {
            return tempUser;
        }
        return null;
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
