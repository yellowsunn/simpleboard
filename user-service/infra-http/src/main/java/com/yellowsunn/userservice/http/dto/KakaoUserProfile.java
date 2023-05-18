package com.yellowsunn.userservice.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserProfile(
        @JsonProperty("thumbnail_image_url")
        String thumbnail
) {
}
