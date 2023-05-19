package com.yellowsunn.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ACCESS_TOKEN_EXPIRED("로그인이 만료되었습니다.", 403),
    UNKNOWN_ERROR("알 수 없는 에러가 발생하였습니다.", 500),
    ;

    private final String description;
    private final Integer status;

    ErrorCode(String description, Integer status) {
        this.description = description;
        this.status = status;
    }
}
