package com.yellowsunn.userservice.http.dto;

public record GoogleOAuth2UserInfoResponseDto(
        String email,
        String picture
) {
}
