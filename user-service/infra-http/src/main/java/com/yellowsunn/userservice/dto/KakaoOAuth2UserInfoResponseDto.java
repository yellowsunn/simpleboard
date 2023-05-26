package com.yellowsunn.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoOAuth2UserInfoResponseDto(
        @JsonProperty("kakao_account")
        KakaoUserInfo userInfo
) {
}
