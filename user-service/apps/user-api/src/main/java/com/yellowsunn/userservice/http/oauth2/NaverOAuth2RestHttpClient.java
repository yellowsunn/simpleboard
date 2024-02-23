package com.yellowsunn.userservice.http.oauth2;

import com.yellowsunn.userservice.constant.OAuth2Request;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.dto.NaverOAuth2UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class NaverOAuth2RestHttpClient implements OAuth2UserInfoHttpClient {
    private final RestTemplate restTemplate;

    private static final String BASE_URI = "https://openapi.naver.com";

    @Override
    public OAuth2Type type() {
        return OAuth2Type.NAVER;
    }

    @Override
    public OAuth2UserInfo findUserInfo(String token, OAuth2Request oAuth2Request) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var request = new HttpEntity<>(headers);
        var uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .path("/v1/nid/me")
                .build()
                .toUri();
        try {
            var responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, NaverOAuth2UserInfoResponseDto.class);
            var naverUserInfo = responseEntity.getBody().response();
            return new OAuth2UserInfo(naverUserInfo.email(), naverUserInfo.profileImage());
        } catch (Exception e) {
            throw new IllegalStateException("네이버 회원 정보를 조회하는데 실패하였습니다.", e);
        }
    }
}
