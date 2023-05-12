package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EmailLoginRequestDto {
    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 24)
    private String password;

    public UserEmailLoginCommand toUserLoginCommand() {
        return new UserEmailLoginCommand(email, password);
    }
}
