package com.yellowsunn.userservice.controller.request;

import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_REGEX;

public record EmailSignUpRequest(
        @NotBlank @Email(message = "이메일 형식이 올바르지 않습니다.") String email,
        @NotBlank @Pattern(regexp = VALID_PASSWORD_REGEX, message = VALID_PASSWORD_MESSAGE) String password,
        @NotBlank @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE) String nickName
) {
    public UserEmailSignUpCommand toUserSignUpCommand() {
        return new UserEmailSignUpCommand(email, password, nickName);
    }
}
