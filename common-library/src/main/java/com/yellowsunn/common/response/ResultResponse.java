package com.yellowsunn.common.response;

public record ResultResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    public static <T> ResultResponse<T> ok(T data) {
        return new ResultResponse<>(true, "SUCCESS", "", data);
    }

    public static ResultResponse<Void> failed(String code, String message) {
        return new ResultResponse<>(false, code, message, null);
    }

    public static ResultResponse<Void> requireLogin() {
        return failed("REQUIRE_LOGIN", "로그인이 필요합니다.");
    }

    public static ResultResponse<Void> accessTokenExpired() {
        return failed("ACCESS_TOKEN_EXPIRED", "로그인이 만료되었습니다.");
    }

    public static ResultResponse<Void> notFoundUser() {
        return failed("NOT_FOUND_USER", "유저를 찾을 수 없습니다.");
    }

    public static ResultResponse<Void> unknownError() {
        return failed("UNKNOWN_ERROR", "알 수 없는 에러가 발생하였습니다.");
    }
}
