package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{3,50}$", message = "3~50자의 영문소문자, 숫자 조합만 가능합니다.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9~`!@#$%^&*()-+=]{8,24}$", message = "8~24자의 영문대소문자, 숫자, 특수문자만 가능합니다.")
    private String password;
}
