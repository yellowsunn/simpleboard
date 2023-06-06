package com.yellowsunn.userservice.dto;

import com.yellowsunn.userservice.constant.OAuth2Type;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OAuth2LinkUserRequestDto {
    // OAuth2 Token
    @NotBlank
    private String token;

    // OAuth2 Type
    @NotBlank
    private String type;

    public UserOAuth2LinkCommand toCommand(Long userId) {
        return UserOAuth2LinkCommand.builder()
                .userId(userId)
                .oAuth2Token(token)
                .type(OAuth2Type.convertFrom(type))
                .build();
    }
}
