package com.yellowsunn.userservice.exception;

public class EmailLoginFailedException extends IllegalStateException {

    public EmailLoginFailedException() {
        super("가입하지 않은 이메일이거나, 잘못된 비밀번호입니다.");
    }
}
