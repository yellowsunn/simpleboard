package com.yellowsunn.userservice.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(ErrorCode code, String message) {
}
