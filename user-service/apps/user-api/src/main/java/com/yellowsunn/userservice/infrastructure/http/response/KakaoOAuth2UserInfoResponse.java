package com.yellowsunn.userservice.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2UserInfoResponse(
        @JsonProperty("kakao_account")
        KakaoUserInfo userInfo
) {

    public record KakaoUserInfo(
            String email,
            KakaoUserProfile profile
    ) {

        public record KakaoUserProfile(
                @JsonProperty("thumbnail_image_url")
                String thumbnail
        ) {

        }
    }
}
