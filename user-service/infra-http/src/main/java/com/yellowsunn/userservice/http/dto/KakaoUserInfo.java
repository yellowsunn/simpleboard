package com.yellowsunn.userservice.http.dto;

public record KakaoUserInfo(
        String email,
        KakaoUserProfile profile
) {
}
