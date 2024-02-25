package com.yellowsunn.userservice.infrastructure.http.oauth2;

import com.yellowsunn.userservice.constant.OAuth2Request;
import com.yellowsunn.userservice.constant.OAuth2Type;

public interface OAuth2UserInfoHttpClient {

    OAuth2Type type();

    OAuth2UserInfo findUserInfo(String token, OAuth2Request oAuth2Request);
}
