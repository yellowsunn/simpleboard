package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.constant.OAuth2Type;
import jakarta.validation.constraints.NotBlank;

/**
 * @param token OAuth2 Token
 * @param type  OAuth2 Type
 * @param state CSRF Token (회원가입 시 확인하는데 사용)
 */
public record OAuth2LoginOrSignUpRequestDto(
        @NotBlank String token,
        @NotBlank String type,
        @NotBlank String state
) {
    public UserOAuth2LoginOrSignUpCommand toCommand() {
        return UserOAuth2LoginOrSignUpCommand.builder()
                .oAuth2Token(token)
                .type(OAuth2Type.convertFrom(type))
                .csrfToken(state)
                .build();
    }
}
