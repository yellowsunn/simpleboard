package com.yellowsunn.common.exception;

import lombok.Getter;

@Getter
public class ExpiredAccessTokenException extends IllegalStateException {

    private final String userId;

    public ExpiredAccessTokenException(Throwable e, String userId) {
        super("액세스 토큰이 만료되었습니다.", e);
        this.userId = userId;
    }
}
