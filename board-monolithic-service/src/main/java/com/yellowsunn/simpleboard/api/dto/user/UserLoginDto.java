package com.yellowsunn.simpleboard.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
