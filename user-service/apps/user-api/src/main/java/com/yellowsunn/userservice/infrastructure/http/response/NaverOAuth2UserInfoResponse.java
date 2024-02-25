package com.yellowsunn.userservice.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverOAuth2UserInfoResponse(
        NaverUserInfo response
) {

    public record NaverUserInfo(
            String email,
            @JsonProperty("profile_image")
            String profileImage
    ) {

    }
}
