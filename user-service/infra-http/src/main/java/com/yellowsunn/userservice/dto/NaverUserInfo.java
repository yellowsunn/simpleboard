package com.yellowsunn.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUserInfo(
        String email,
        @JsonProperty("profile_image")
        String profileImage
) {
}
