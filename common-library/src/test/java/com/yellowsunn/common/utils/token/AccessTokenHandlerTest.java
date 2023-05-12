package com.yellowsunn.common.utils.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenHandlerTest {
    AccessTokenHandler accessTokenHandler;

    @BeforeEach
    void setUp() {
        accessTokenHandler = new AccessTokenHandler("fTjWnZq4t7w!z%C*F-JaNdRgUkXp2s5u", Duration.ofSeconds(5));
    }

    @Test
    void generate_and_parse_token_test() {
        // given
        var payload = AccessTokenPayload.builder()
                .uuid("uuid")
                .email("test@example.com")
                .build();

        // when
        String encodedToken = accessTokenHandler.generateEncodedToken(payload);
        AccessTokenPayload accessTokenPayload = accessTokenHandler.parseEncodedToken(encodedToken);

        // then
        assertThat(accessTokenPayload.uuid()).isEqualTo(payload.uuid());
        assertThat(accessTokenPayload.email()).isEqualTo(payload.email());
    }
}
