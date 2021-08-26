package com.yellowsunn.simpleforum.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDto {

    @NotBlank
    @Size(max = 16)
    @Pattern(regexp = "^[a-z0-9]+$", message = "영문소문자 + 숫자 조합만 가능합니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
