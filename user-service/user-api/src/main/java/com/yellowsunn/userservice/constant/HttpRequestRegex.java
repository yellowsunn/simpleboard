package com.yellowsunn.userservice.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpRequestRegex {
    VALID_PASSWORD_REGEX("^[a-zA-Z0-9~`!@#$%^&*()-+=]{8,24}$", "8~24자의 영문대소문자, 숫자, 특수문자만 가능합니다."),
    VALID_NICKNAME_REGEX("^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$", "사용할 수 없는 닉네임입니다."),
    ;

    private final String regex;
    private final String message;
}
