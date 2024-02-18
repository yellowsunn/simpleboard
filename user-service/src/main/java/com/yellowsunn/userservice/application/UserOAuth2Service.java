package com.yellowsunn.userservice.application;

import com.yellowsunn.userservice.domain.dto.OAuth2UserInfoDto;
import com.yellowsunn.userservice.domain.port.OAuth2HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOAuth2Service {

    private final OAuth2HttpClient oAuth2HttpClient;

    public OAuth2UserInfoDto getGoogleUserInfo(String token) {
        return oAuth2HttpClient.findGoogleUserInfo(token)
                .orElseThrow(() -> new IllegalStateException("구글 계정으로 회원정보를 찾을 수 없습니다."));
    }

    public OAuth2UserInfoDto getNaverUserInfo(String token) {
        return oAuth2HttpClient.findNaverUserInfo(token)
                .orElseThrow(() -> new IllegalStateException("네이버 계정으로 회원정보를 찾을 수 없습니다."));
    }

    public OAuth2UserInfoDto getKakaoUserInfo(String code, String clientId, String redirectUrl) {
        return oAuth2HttpClient.findKakaoUserInfo(code, clientId, redirectUrl)
                .orElseThrow(() -> new IllegalStateException("카카오 계정으로 회원정보를 찾을 수 없습니다."));
    }
}
