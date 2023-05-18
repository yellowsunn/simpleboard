package com.yellowsunn.userservice.http.client;

import com.yellowsunn.userservice.constant.OAuth2Request;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.http.OAuth2UserInfo;
import com.yellowsunn.userservice.http.dto.KakaoOAuth2TokenResponseDto;
import com.yellowsunn.userservice.http.dto.KakaoOAuth2UserInfoResponseDto;
import com.yellowsunn.userservice.http.dto.KakaoUserInfo;
import com.yellowsunn.userservice.http.dto.KakaoUserProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class KakaoOAuth2RestHttpClient implements OAuth2UserInfoHttpClient {
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String loginRedirectUri;
    private final String userLinkRedirectUri;

    private static final String KAKAO_AUTH_BASE_URI = "https://kauth.kakao.com";
    private static final String KAKAO_API_BASE_URI = "https://kapi.kakao.com";

    public KakaoOAuth2RestHttpClient(RestTemplate restTemplate,
                                     @Value("${oauth2.kakao.client-id}") String clientId,
                                     @Value("${oauth2.kakao.redirect-uri.login}") String loginRedirectUri,
                                     @Value("${oauth2.kakao.redirect-uri.user-link}") String userLinkRedirectUri) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.loginRedirectUri = loginRedirectUri;
        this.userLinkRedirectUri = userLinkRedirectUri;
    }

    @Override
    public OAuth2Type type() {
        return OAuth2Type.KAKAO;
    }

    @Override
    public OAuth2UserInfo findUserInfo(String code, OAuth2Request oAuth2Request) {
        var accessToken = getTokenByCode(code, oAuth2Request);

        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var request = new HttpEntity<>(headers);

        var uri = UriComponentsBuilder.fromUriString(KAKAO_API_BASE_URI)
                .path("/v2/user/me")
                .build()
                .toUri();
        try {
            var responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, KakaoOAuth2UserInfoResponseDto.class);
            KakaoUserInfo kakaoUserInfo = responseEntity.getBody().userInfo();

            if (StringUtils.isBlank(kakaoUserInfo.email())) {
                revokeAgreement(accessToken);
            }
            return new OAuth2UserInfo(kakaoUserInfo.email(), getThumbnail(kakaoUserInfo.profile()));
        } catch (Exception e) {
            throw new IllegalStateException("카카오 회원 정보를 조회하는데 실패하였습니다.", e);
        }
    }

    private String getTokenByCode(String code, OAuth2Request oAuth2Request) {
        var params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", getRedirectUri(oAuth2Request));
        params.add("code", code);

        var uri = UriComponentsBuilder.fromUriString(KAKAO_AUTH_BASE_URI)
                .path("/oauth/token")
                .build()
                .toUri();
        try {
            var kakaoOAuth2TokenResponseDto = restTemplate.postForObject(uri, params, KakaoOAuth2TokenResponseDto.class);
            return kakaoOAuth2TokenResponseDto.accessToken();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 토큰을 조회하는데 실패하였습니다.", e);
        }
    }

    // 동의 철회하기 (이메일 제공에 동의하지 않은 경우 철회시킨다)
    private void revokeAgreement(String accessToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var params = new LinkedMultiValueMap<>();
        params.add("scopes", "[\"account_email\"]");

        var request = new HttpEntity<>(params, headers);

        var uri = UriComponentsBuilder.fromUriString(KAKAO_API_BASE_URI)
                .path("/v2/user/revoke/scopes")
                .build()
                .toUri();

        try {
            restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.error("동의 철회에 실패하였습니다.");
        }
    }

    private String getThumbnail(KakaoUserProfile profile) {
        if (profile == null) {
            return null;
        }
        return profile.thumbnail();
    }

    private String getRedirectUri(OAuth2Request oAuth2Request) {
        return switch (oAuth2Request) {
            case LOGIN -> loginRedirectUri;
            case USER_LINK -> userLinkRedirectUri;
        };
    }
}
