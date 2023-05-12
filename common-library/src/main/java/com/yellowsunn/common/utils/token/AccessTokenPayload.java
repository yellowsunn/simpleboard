package com.yellowsunn.common.utils.token;

import lombok.Builder;

@Builder
public record AccessTokenPayload(
        String uuid,
        String email
) {
}
