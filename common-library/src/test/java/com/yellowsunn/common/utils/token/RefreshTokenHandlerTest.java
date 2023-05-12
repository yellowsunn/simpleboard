package com.yellowsunn.common.utils.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenHandlerTest {

    RefreshTokenHandler refreshTokenHandler;

    @BeforeEach
    void setUp() {
        refreshTokenHandler = new RefreshTokenHandler("fTjWnZq4t7w!z%C*F-JaNdRgUkXp2s5u", Duration.ofSeconds(5));
    }

    @Test
    void generateEncodedToken() {
        // when
        String token = refreshTokenHandler.generateEncodedToken();

        // then
        assertThat(token).isNotBlank().isBase64();
    }
}
