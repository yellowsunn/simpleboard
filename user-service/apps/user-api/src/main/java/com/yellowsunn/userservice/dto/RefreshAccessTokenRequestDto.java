package com.yellowsunn.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessTokenRequestDto(
        @NotBlank String accessToken,
        @NotBlank String refreshToken
) {
}
