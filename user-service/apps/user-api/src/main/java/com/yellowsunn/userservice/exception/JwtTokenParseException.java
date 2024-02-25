package com.yellowsunn.userservice.exception;

public class JwtTokenParseException extends RuntimeException {

    public JwtTokenParseException(Throwable cause) {
        super("Failed to parse jwt oAuth2Token", cause);
    }
}
