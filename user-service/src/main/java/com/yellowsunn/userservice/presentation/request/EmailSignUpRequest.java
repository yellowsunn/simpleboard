package com.yellowsunn.userservice.presentation.request;

public record EmailSignUpRequest(
        String email,
        String password,
        String nickname
) {

}
