package com.yellowsunn.userservice.constant;

import com.yellowsunn.userservice.domain.user.Provider;
import org.apache.commons.lang3.EnumUtils;

public enum OAuth2Type {
    GOOGLE,
    NAVER,
    KAKAO,
    ;

    public static OAuth2Type convertFrom(String type) {
        OAuth2Type oAuth2Type = EnumUtils.getEnumIgnoreCase(OAuth2Type.class, type);
        if (oAuth2Type == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 타입입니다.");
        }
        return oAuth2Type;
    }

    public Provider toProvider() {
        return switch (this) {
            case GOOGLE -> Provider.GOOGLE;
            case NAVER -> Provider.NAVER;
            case KAKAO -> Provider.KAKAO;
        };
    }
}
