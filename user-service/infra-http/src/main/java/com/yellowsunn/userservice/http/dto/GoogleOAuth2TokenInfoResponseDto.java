package com.yellowsunn.userservice.http.dto;

public record GoogleOAuth2TokenInfoResponseDto(
        String email,
        String picture
) {
}
