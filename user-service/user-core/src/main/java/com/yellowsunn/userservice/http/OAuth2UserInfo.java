package com.yellowsunn.userservice.http;

public record OAuth2UserInfo(
        String email,
        String thumbnail
) {
}
