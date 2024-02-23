package com.yellowsunn.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2TokenResponseDto(
        @JsonProperty("access_token")
        String accessToken
) {
}
