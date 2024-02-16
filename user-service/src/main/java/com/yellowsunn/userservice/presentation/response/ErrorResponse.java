package com.yellowsunn.userservice.presentation.response;

public record ErrorResponse(
        int code,
        String message
) {

    public static ErrorResponse fromErrorMessage(ErrorMessage errorMessage) {
        return new ErrorResponse(errorMessage.getCode(), errorMessage.getMessage());
    }
}
