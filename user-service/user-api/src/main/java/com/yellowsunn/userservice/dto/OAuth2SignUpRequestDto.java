package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;

@Getter
public class OAuth2SignUpRequestDto {
    private String state;

    @NotBlank
    private String tempUserToken;

    @NotBlank
    @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE)
    private String nickName;

    public UserOAuth2SignUpCommand toUserOAuth2SignUpCommand() {
        return UserOAuth2SignUpCommand.builder()
                .csrfToken(StringUtils.defaultString(state))
                .tempUserToken(tempUserToken)
                .nickName(nickName)
                .build();
    }
}
