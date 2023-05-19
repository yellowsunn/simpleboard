package com.yellowsunn.common.utils.token;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.common.utils.Base64Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

public class RefreshTokenParser {
    protected final String secret;
    protected final ObjectMapper objectMapper;

    public RefreshTokenParser(String secret) {
        this.secret = secret;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String parseEncodedToken(String encodedToken) {
        String token = Base64Handler.decode(encodedToken);

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build();

        Jws<Claims> claims = jwtParser.parseClaimsJws(token);
        return claims.getBody().getId();
    }
}
