package com.yellowsunn.simpleforum.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
