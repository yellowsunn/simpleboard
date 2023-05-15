package com.yellowsunn.userservice.http.client;

import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.http.OAuth2UserInfo;

public interface OAuth2UserInfoHttpClient {
    OAuth2Type type();

    OAuth2UserInfo findUserInfo(String token);
}
