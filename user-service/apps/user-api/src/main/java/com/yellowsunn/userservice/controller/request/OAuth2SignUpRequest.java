package com.yellowsunn.userservice.controller.request;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;

import com.yellowsunn.userservice.application.command.UserOAuth2SignUpCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;

public record OAuth2SignUpRequest(
        String state,

        @NotBlank
        String tempUserToken,

        @NotBlank
        @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE)
        String nickName
) {

    public UserOAuth2SignUpCommand toUserOAuth2SignUpCommand() {
        return UserOAuth2SignUpCommand.builder()
                .csrfToken(StringUtils.defaultString(state))
                .tempUserToken(tempUserToken)
                .nickName(nickName)
                .build();
    }
}
