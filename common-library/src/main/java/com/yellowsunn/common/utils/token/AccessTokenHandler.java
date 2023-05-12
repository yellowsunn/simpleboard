package com.yellowsunn.common.utils.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yellowsunn.common.utils.Base64Handler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class AccessTokenHandler extends AccessTokenParser {
    private final Duration expiration;

    public AccessTokenHandler(String secret, Duration expiration) {
        super(secret);
        this.expiration = expiration;
    }

    public String generateEncodedToken(AccessTokenPayload tokenPayload) {
        var now = Instant.now();
        var payload = this.objectMapper.convertValue(tokenPayload, new TypeReference<Map<String, Object>>() {
        });

        var jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(payload)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        return Base64Handler.encode(jwt);
    }
}
