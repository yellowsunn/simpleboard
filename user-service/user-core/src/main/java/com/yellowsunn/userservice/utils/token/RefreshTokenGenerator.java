package com.yellowsunn.userservice.utils.token;

import com.yellowsunn.userservice.utils.Base64Handler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class RefreshTokenGenerator {
    private final String secret;
    private final Duration expiration;

    public RefreshTokenGenerator(@Value("${token.refresh.secret}") String secret,
                                 @Value("${token.refresh.expiration}") Duration expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public String generateEncodedToken() {
        var now = Instant.now();
        var jti = UUID.randomUUID().toString();

        var jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setId(jti)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        return Base64Handler.encode(jwt);
    }
}
