package com.yellowsunn.userservice.utils.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenGeneratorTest {

    RefreshTokenGenerator refreshTokenGenerator;

    @BeforeEach
    void setUp() {
        refreshTokenGenerator = new RefreshTokenGenerator("fTjWnZq4t7w!z%C*F-JaNdRgUkXp2s5u", Duration.ofSeconds(5));
    }

    @Test
    void generateEncodedToken() {
        // when
        String token = refreshTokenGenerator.generateEncodedToken();

        // then
        assertThat(token).isNotBlank().isBase64();
    }
}
