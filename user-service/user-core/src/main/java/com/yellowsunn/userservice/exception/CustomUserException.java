package com.yellowsunn.userservice.exception;

import lombok.Getter;

@Getter
public class CustomUserException extends RuntimeException {
    private final UserErrorCode errorCode;

    public CustomUserException(UserErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public CustomUserException(UserErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }

    public CustomUserException(UserErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomUserException(UserErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
