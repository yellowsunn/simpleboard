package com.yellowsunn.userservice.presentation.request;

public record EmailLoginRequest(
        String email,
        String password
) {

}
