package com.yellowsunn.userservice.utils.token;

public record AccessTokenPayload(String userId, String email) {
}
