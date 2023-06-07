package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.constant.OAuth2Type;
import jakarta.validation.constraints.NotBlank;

/**
 * @param token OAuth2 Token
 * @param type  OAuth2 Type
 */
public record OAuth2LinkUserRequestDto(
        @NotBlank String token,
        @NotBlank String type
) {
    public UserOAuth2LinkCommand toCommand(Long userId) {
        return UserOAuth2LinkCommand.builder()
                .userId(userId)
                .oAuth2Token(token)
                .type(OAuth2Type.convertFrom(type))
                .build();
    }
}
