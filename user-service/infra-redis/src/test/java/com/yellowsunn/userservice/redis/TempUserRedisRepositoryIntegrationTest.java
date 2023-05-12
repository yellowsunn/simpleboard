package com.yellowsunn.userservice.redis;

import com.yellowsunn.userservice.domain.user.TempUser;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.redis.config.RedisConfiguration;
import com.yellowsunn.userservice.redis.config.TestRedisConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestRedisConfiguration.class, RedisConfiguration.class})
class TempUserRedisRepositoryIntegrationTest {

    @Autowired
    RedisTemplate<String, TempUser> redisTemplate;

    TempUserRedisRepository sut;

    @BeforeEach
    void setUp() {
        sut = new TempUserRedisRepository(redisTemplate);
    }

    @Test
    void save_and_findByToken_test() {
        TempUser tempUser = TempUser.builder()
                .email("test@example.com")
                .provider(Provider.EMAIL)
                .build();

        sut.save(tempUser, Duration.ofSeconds(3L));
        TempUser foundTempUser = sut.findByToken(tempUser.getToken());

        assertThat(foundTempUser.getEmail()).isEqualTo(tempUser.getEmail());
        assertThat(foundTempUser.getProvider()).isEqualTo(tempUser.getProvider());
    }
}
