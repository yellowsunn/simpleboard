package com.yellowsunn.userservice.utils.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenGeneratorTest {

    AccessTokenGenerator accessTokenGenerator;

    @BeforeEach
    void setUp() {
        accessTokenGenerator = new AccessTokenGenerator("fTjWnZq4t7w!z%C*F-JaNdRgUkXp2s5u", Duration.ofSeconds(5));
    }

    @Test
    void generateEncodedToken() {
        // given
        var payload = AccessTokenPayload.builder()
                .uuid("uuid")
                .email("test@example.com")
                .build();

        // when
        String token = accessTokenGenerator.generateEncodedToken(payload);

        // then
        assertThat(token).isNotBlank().isBase64();
    }
}
