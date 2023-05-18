package com.yellowsunn.userservice.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUserInfo(
        String email,
        @JsonProperty("profile_image")
        String profileImage
) {
}
