package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailSignUpRequestDto {
    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9~`!@#$%^&*()-+=]{8,24}$", message = "8~24자의 영문대소문자, 숫자, 특수문자만 가능합니다.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$", message = "사용할 수 없는 닉네임입니다.")
    private String nickName;

    public UserEmailSignUpCommand toUserSignUpCommand() {
        return new UserEmailSignUpCommand(email, password, nickName);
    }
}
