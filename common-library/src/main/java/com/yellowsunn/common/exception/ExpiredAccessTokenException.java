package com.yellowsunn.common.exception;

import lombok.Getter;

@Getter
public class ExpiredAccessTokenException extends IllegalStateException {
    private final String userUUID;
    private final String email;

    public ExpiredAccessTokenException(Throwable e, String userUUID, String email) {
        super("액세스 토큰이 만료되었습니다.", e);
        this.userUUID = userUUID;
        this.email = email;
    }
}
