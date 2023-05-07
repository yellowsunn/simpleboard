package com.yellowsunn.userservice.exception;

import lombok.Getter;

@Getter
public enum UserErrorCode {
    ALREADY_EXIST_NICKNAME("이미 존재하는 닉네임입니다.", 400),
    ALREADY_EXIST_EMAIL("이미 가입된 계정이 존재합니다.", 400),
    INVALID_LOGIN("가입하지 않은 이메일이거나, 잘못된 비밀번호입니다.", 400),
    NOT_FOUND_USER("유저를 찾을 수 없습니다.", 404),
    ;

    private final String description;
    private final int status;

    UserErrorCode(String description, int status) {
        this.description = description;
        this.status = status;
    }
}
