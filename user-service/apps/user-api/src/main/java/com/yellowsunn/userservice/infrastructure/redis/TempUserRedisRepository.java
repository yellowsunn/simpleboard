package com.yellowsunn.userservice.infrastructure.redis;

import com.yellowsunn.userservice.application.port.TempUserCacheRepository;
import com.yellowsunn.userservice.domain.user.TempUser;
import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
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
    public Optional<TempUser> findByTokenAndCsrfToken(String token, String csrfToken) {
        String key = generateKey(token);
        TempUser tempUser = redisTemplate.opsForValue().get(key);

        return Optional.ofNullable(tempUser)
                .filter(it -> StringUtils.equals(tempUser.getCsrfToken(), csrfToken));
    }

    @Override
    public void deleteByToken(String token) {
        String key = generateKey(token);
        Boolean result = redisTemplate.delete(key);
        if (BooleanUtils.isNotTrue(result)) {
            log.error("Redis failed to delete key. key={}", key);
        }
    }

    private String generateKey(String token) {
        return PREFIX_KEY + token;
    }
}
