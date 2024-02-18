package com.yellowsunn.userservice.presentation.request;

public record KakaoUserInfoRequest(
        String token,
        String clientId,
        String redirectUrl
) {

}
