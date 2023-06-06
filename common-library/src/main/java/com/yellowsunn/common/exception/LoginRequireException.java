package com.yellowsunn.common.exception;

public class LoginRequireException extends RuntimeException {
    public LoginRequireException() {
        super("로그인이 필요합니다.");
    }
}
