package com.yellowsunn.simpleforum.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDto {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자 사이어야 합니다.")
    private String password;
}
