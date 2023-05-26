package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_NICKNAME_REGEX;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_MESSAGE;
import static com.yellowsunn.userservice.constant.HttpRequestConst.VALID_PASSWORD_REGEX;

@Getter
public class EmailSignUpRequestDto {
    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = VALID_PASSWORD_REGEX, message = VALID_PASSWORD_MESSAGE)
    private String password;

    @NotBlank
    @Pattern(regexp = VALID_NICKNAME_REGEX, message = VALID_NICKNAME_MESSAGE)
    private String nickName;

    public UserEmailSignUpCommand toUserSignUpCommand() {
        return new UserEmailSignUpCommand(email, password, nickName);
    }
}
