package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshAccessTokenRequestDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
