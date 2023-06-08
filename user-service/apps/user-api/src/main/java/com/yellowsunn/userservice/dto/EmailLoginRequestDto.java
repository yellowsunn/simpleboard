package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_REGEX;

public record EmailLoginRequestDto(
        @NotBlank @Email(message = "이메일 형식이 올바르지 않습니다.") String email,
        @NotBlank @Pattern(regexp = VALID_PASSWORD_REGEX, message = VALID_PASSWORD_MESSAGE) String password
) {
    public UserEmailLoginCommand toUserLoginCommand() {
        return new UserEmailLoginCommand(email, password);
    }
}
