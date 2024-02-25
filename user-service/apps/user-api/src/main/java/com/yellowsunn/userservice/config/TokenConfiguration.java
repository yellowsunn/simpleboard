package com.yellowsunn.userservice.config;

import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfiguration {

    @Bean
    public AccessTokenHandler accessTokenHandler(
            @Value("${token.access.secret}") String secret,
            @Value("${token.access.expiration}") Duration expiration
    ) {
        return new AccessTokenHandler(secret, expiration);
    }

    @Bean
    public RefreshTokenHandler refreshTokenHandler(
            @Value("${token.refresh.secret}") String secret,
            @Value("${token.refresh.expiration}") Duration expiration
    ) {
        return new RefreshTokenHandler(secret, expiration);
    }
}
