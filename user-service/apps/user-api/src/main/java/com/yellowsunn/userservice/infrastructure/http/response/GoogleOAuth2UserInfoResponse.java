package com.yellowsunn.userservice.infrastructure.http.response;

public record GoogleOAuth2UserInfoResponse(
        String email,
        String picture
) {

}
