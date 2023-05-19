package com.yellowsunn.common.utils.token;

import com.yellowsunn.common.utils.Base64Handler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class RefreshTokenHandler extends RefreshTokenParser {
    private final Duration expiration;

    public RefreshTokenHandler(String secret, Duration expiration) {
        super(secret);
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
