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
public class UserPatchRequestDto {

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;
}
