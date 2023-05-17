package com.yellowsunn.userservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpRequestConst {
    public static final String VALID_PASSWORD_REGEX = "^[a-zA-Z0-9~`!@#$%^&*()-+=]{8,24}$";
    public static final String VALID_PASSWORD_MESSAGE = "8~24자의 영문대소문자, 숫자, 특수문자만 가능합니다.";

    public static final String VALID_NICKNAME_REGEX = "^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$";
    public static final String VALID_NICKNAME_MESSAGE = "3~20자의 한글, 영어 소문자, 숫자 조합만 입력 가능합니다.";
}
