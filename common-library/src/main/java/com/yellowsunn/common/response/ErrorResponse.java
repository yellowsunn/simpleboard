package com.yellowsunn.common.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        Integer status,
        String code,
        String message
) {
    public static ErrorResponse requireLogin() {
        return ErrorResponse.builder()
                .status(401)
                .code("REQUIRE_LOGIN")
                .message("로그인이 필요합니다.")
                .build();
    }

    public static ErrorResponse accessTokenExpired() {
        return ErrorResponse.builder()
                .status(403)
                .code("ACCESS_TOKEN_EXPIRED")
                .message("로그인이 만료되었습니다.")
                .build();
    }

    public static ErrorResponse notFoundUser() {
        return ErrorResponse.builder()
                .status(401)
                .code("NOT_FOUND_USER")
                .message("유저를 찾을 수 없습니다.")
                .build();
    }

    public static ErrorResponse unknownError() {
        return ErrorResponse.builder()
                .status(500)
                .code("UNKNOWN_ERROR")
                .message("알 수 없는 에러가 발생하였습니다.")
                .build();
    }
}
