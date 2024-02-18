package com.yellowsunn.userservice.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2TokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}
