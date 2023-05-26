package com.yellowsunn.userservice.exception;

import lombok.Getter;

@Getter
public enum UserErrorCode {
    ALREADY_EXIST_NICKNAME("이미 존재하는 닉네임입니다.", 400),
    ALREADY_EXIST_EMAIL("이미 존재하는 이메일입니다.", 400),
    INVALID_LOGIN("가입하지 않은 이메일이거나, 잘못된 비밀번호입니다.", 400),
    INVALID_TEMP_USER("임시 유저 정보를 확인할 수 없습니다.", 400),
    NOT_FOUND_OAUTH_PROVIDER_EMAIL("소셜 계정을 사용하기 위해서는 이메일 조회 동의가 필요합니다.", 400),
    LINK_AT_OTHER_USER_PROVIDER("이미 다른 계정에 연동되어 있습니다.", 400),
    LINK_AT_LEAST_ONE_USER_PROVIDER("최소 1개의 계정이 연결되어 있어야 합니다.", 400),
    REQUIRED_LOGIN("로그인이 필요합니다.", 401),
    ;

    private final String description;
    private final int status;

    UserErrorCode(String description, int status) {
        this.description = description;
        this.status = status;
    }
}