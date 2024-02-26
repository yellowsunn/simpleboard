package com.yellowsunn.userservice.domain.user;

import org.springframework.util.Assert;

public enum Provider {
    EMAIL,
    GOOGLE,
    NAVER,
    KAKAO,
    ;

    public static void checkOAuth2(Provider provider) {
        boolean isOAuth2 = provider == GOOGLE || provider == NAVER || provider == KAKAO;
        Assert.isTrue(isOAuth2, () -> "oauth2 provider가 아닙니다.");
    }
}
