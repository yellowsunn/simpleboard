package com.yellowsunn.userservice.utils.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

@Component
public class AccessTokenGenerator {
    private final String secret;
    private final Duration expiration;
    private final ObjectMapper objectMapper;

    public AccessTokenGenerator(@Value("${token.access.secret}") String secret,
                                @Value("${token.access.expiration}") Duration expiration) {
        this.secret = secret;
        this.expiration = expiration;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
