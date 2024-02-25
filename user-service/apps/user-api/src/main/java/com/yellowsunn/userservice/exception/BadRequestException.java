package com.yellowsunn.userservice.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends IllegalArgumentException {

    private final UserErrorCode code;

    public BadRequestException(UserErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
