package com.yellowsunn.userservice.utils.token;

import lombok.Builder;

@Builder
public record AccessTokenPayload(String uuid, String email) {
}
