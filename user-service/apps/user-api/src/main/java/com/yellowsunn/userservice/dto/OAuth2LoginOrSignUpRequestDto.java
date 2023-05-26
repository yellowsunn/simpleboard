package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.constant.OAuth2Type;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OAuth2LoginOrSignUpRequestDto {
    // OAuth2 Token
    @NotBlank
    private String token;

    // OAuth2 Type
    @NotBlank
    private String type;

    // CSRF Token (회원가입 시 확인하는데 사용)
    @NotBlank
    private String state;

    public UserOAuth2LoginOrSignUpCommand toCommand() {
        return UserOAuth2LoginOrSignUpCommand.builder()
                .oAuth2Token(token)
                .type(OAuth2Type.convertFrom(type))
                .csrfToken(state)
                .build();
    }
}
