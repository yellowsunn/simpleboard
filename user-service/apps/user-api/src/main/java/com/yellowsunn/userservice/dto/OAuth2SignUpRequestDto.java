package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;

public record OAuth2SignUpRequestDto(
        String state,
        @NotBlank String tempUserToken,
        @NotBlank @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE) String nickName
) {

    public UserOAuth2SignUpCommand toUserOAuth2SignUpCommand() {
        return UserOAuth2SignUpCommand.builder()
                .csrfToken(StringUtils.defaultString(state))
                .tempUserToken(tempUserToken)
                .nickName(nickName)
                .build();
    }
}
