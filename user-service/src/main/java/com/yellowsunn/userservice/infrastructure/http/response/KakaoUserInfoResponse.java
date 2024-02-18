package com.yellowsunn.userservice.infrastructure.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            String email,
            KakaoProfile profile
    ) {

        public record KakaoProfile(
                @JsonProperty("thumbnail_image_url")
                String thumbnailImageUrl
        ) {

        }

        public String thumbnail() {
            if (profile == null) {
                return null;
            }

            return profile.thumbnailImageUrl;
        }
    }
}
