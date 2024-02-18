package com.yellowsunn.userservice.infrastructure.http.response;

public record GoogleUserInfoResponse(
        String email,
        String picture
) {

}
