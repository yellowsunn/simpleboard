package com.yellowsunn.common.exception;

import lombok.Getter;

@Getter
public class ExpiredAccessTokenException extends IllegalStateException {
    private final Long userId;
    private final String userUUID;
    private final String email;

    public ExpiredAccessTokenException(Throwable e, Long userId, String userUUID, String email) {
        super("액세스 토큰이 만료되었습니다.", e);
        this.userId = userId;
        this.userUUID = userUUID;
        this.email = email;
    }
}
