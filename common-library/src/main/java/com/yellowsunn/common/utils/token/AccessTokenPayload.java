package com.yellowsunn.common.utils.token;

import lombok.Builder;

@Builder
public record AccessTokenPayload(
        Long id,
        String uuid,
        String email
) {
}
