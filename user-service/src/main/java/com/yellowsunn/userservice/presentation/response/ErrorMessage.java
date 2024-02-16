package com.yellowsunn.userservice.presentation.response;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    UNKNOWN(-10000, "일시적인 장애입니다."),
    INVALIDATION(-10001, "요청에 대한 Validation 에러입니다."),
    BAD_REQUEST(-10002, "잘못된 요청입니다."),
    ;

    private final int code;
    private final String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
