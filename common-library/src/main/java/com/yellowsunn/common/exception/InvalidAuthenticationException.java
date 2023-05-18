package com.yellowsunn.common.exception;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("로그인이 필요합니다.");
    }
}
