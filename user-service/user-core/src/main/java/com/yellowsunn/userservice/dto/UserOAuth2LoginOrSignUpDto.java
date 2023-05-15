package com.yellowsunn.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
public class UserOAuth2LoginOrSignUpDto {
    private final Boolean isLogin;

    @JsonInclude(NON_NULL)
    private String accessToken;

    @JsonInclude(NON_NULL)
    private String refreshToken;

    @JsonInclude(NON_NULL)
    private String tempUserToken;

    // 로그인 시 내려주는 토큰 정보
    @Builder(builderMethodName = "loginBuilder", builderClassName = "LoginBuilder")
    protected UserOAuth2LoginOrSignUpDto(String accessToken, String refreshToken) {
        this.isLogin = true;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // 회원가입이 필요한 경우
    @Builder(builderMethodName = "signUpRequestBuilder", builderClassName = "SignUpRequestBuilder")
    protected UserOAuth2LoginOrSignUpDto(String tempUserToken) {
        this.isLogin = false;
        this.tempUserToken = tempUserToken;
    }
}
