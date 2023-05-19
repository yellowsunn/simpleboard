package com.yellowsunn.common.utils.token;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.common.exception.ExpiredAccessTokenException;
import com.yellowsunn.common.exception.JwtTokenParseException;
import com.yellowsunn.common.utils.Base64Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

public class AccessTokenParser {
    protected final String secret;
    protected final ObjectMapper objectMapper;

    public AccessTokenParser(String secret) {
        this.secret = secret;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AccessTokenPayload parseEncodedToken(String encodedToken) {
        String token = Base64Handler.decode(encodedToken);

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build();

        try {
            Jws<Claims> claims = jwtParser.parseClaimsJws(token);
            String payload = objectMapper.writeValueAsString(claims.getBody());
            return objectMapper.readValue(payload, AccessTokenPayload.class);
        } catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException(e, getUserUUID(e), getEmail(e));
        } catch (Exception e) {
            throw new JwtTokenParseException(e);
        }
    }

    private String getUserUUID(ExpiredJwtException e) {
        if (e.getClaims() != null) {
            return (String) e.getClaims().get("uuid");
        }
        return null;
    }

    private String getEmail(ExpiredJwtException e) {
        if (e.getClaims() != null) {
            return (String) e.getClaims().get("email");
        }
        return null;
    }
}
