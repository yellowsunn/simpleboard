package com.yellowsunn.userservice.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(String code, String message) {
}
