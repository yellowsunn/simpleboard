package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;

public record UserInfoUpdateRequestDto(
        @NotBlank @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE) String nickName
) {
    public UserInfoUpdateCommand toCommand(Long userId) {
        return UserInfoUpdateCommand.builder()
                .userId(userId)
                .nickName(nickName)
                .build();
    }
}
