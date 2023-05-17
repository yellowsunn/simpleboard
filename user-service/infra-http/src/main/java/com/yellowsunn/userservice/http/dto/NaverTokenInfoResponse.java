package com.yellowsunn.userservice.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverTokenInfoResponse(
        String email,
        @JsonProperty("profile_image")
        String profileImage
) {
}
