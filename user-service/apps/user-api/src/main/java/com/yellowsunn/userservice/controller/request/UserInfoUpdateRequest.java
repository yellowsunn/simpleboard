package com.yellowsunn.userservice.controller.request;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;

import com.yellowsunn.userservice.application.command.UserInfoUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserInfoUpdateRequest(
        @NotBlank @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE) String nickName
) {

    public UserInfoUpdateCommand toCommand(Long userId) {
        return UserInfoUpdateCommand.builder()
                .userId(userId)
                .nickName(nickName)
                .build();
    }
}
