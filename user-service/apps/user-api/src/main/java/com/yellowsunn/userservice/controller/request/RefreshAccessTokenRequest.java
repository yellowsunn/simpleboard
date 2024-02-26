package com.yellowsunn.userservice.controller.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessTokenRequest(
        @NotBlank
        String accessToken,

        @NotBlank
        String refreshToken
) {

}
