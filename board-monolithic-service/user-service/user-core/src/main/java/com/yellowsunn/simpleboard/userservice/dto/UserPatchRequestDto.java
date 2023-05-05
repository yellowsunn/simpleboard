package com.yellowsunn.simpleboard.userservice.dto;

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
public class UserPatchRequestDto {

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;
}
