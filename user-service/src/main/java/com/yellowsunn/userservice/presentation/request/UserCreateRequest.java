package com.yellowsunn.userservice.presentation.request;

public record UserCreateRequest(
        String email,
        String password,
        String nickname
) {

}
