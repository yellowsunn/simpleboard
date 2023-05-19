package com.yellowsunn.common.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        Integer status,
        String code,
        String message
) {
}
