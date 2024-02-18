package com.yellowsunn.userservice.infrastructure.http;

import com.yellowsunn.userservice.domain.dto.OAuth2UserInfoDto;
import com.yellowsunn.userservice.domain.port.OAuth2HttpClient;
import com.yellowsunn.userservice.infrastructure.http.feignclient.GoogleOAuth2FeignClient;
import com.yellowsunn.userservice.infrastructure.http.feignclient.KakaoOAuth2FeignClient;
import com.yellowsunn.userservice.infrastructure.http.feignclient.KakaoOAuth2TokenFeignClient;
import com.yellowsunn.userservice.infrastructure.http.feignclient.NaverOAuth2FeignClient;
import com.yellowsunn.userservice.infrastructure.http.response.KakaoOAuth2TokenResponse;
import com.yellowsunn.userservice.infrastructure.http.response.KakaoUserInfoResponse;
import com.yellowsunn.userservice.infrastructure.http.response.NaverUserInfoResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2HttpClientImpl implements OAuth2HttpClient {

    private final GoogleOAuth2FeignClient googleOAuth2FeignClient;
    private final NaverOAuth2FeignClient naverOAuth2FeignClient;
    private final KakaoOAuth2TokenFeignClient kakaoOAuth2TokenFeignClient;
    private final KakaoOAuth2FeignClient kakaoOAuth2FeignClient;

    @Override
    public Optional<OAuth2UserInfoDto> findGoogleUserInfo(String token) {
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }

        return googleOAuth2FeignClient.findGoogleUserInfo(token)
                .map(it -> OAuth2UserInfoDto.builder()
                        .email(it.email())
                        .thumbnail(it.picture())
                        .build()
                );
    }

    @Override
    public Optional<OAuth2UserInfoDto> findNaverUserInfo(String token) {
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }

        return naverOAuth2FeignClient.findUserInfo("Bearer %s".formatted(token))
                .map(NaverUserInfoResponse::response)
                .map(it -> OAuth2UserInfoDto.builder()
                        .email(it.email())
                        .thumbnail(it.profileImage())
                        .build());
    }

    @Override
    public Optional<OAuth2UserInfoDto> findKakaoUserInfo(String code) {
        Map<String, String> data = Map.of(
                "grant_type", "authorization_code",
                "client_id", "36a710c0f0041dcbb3f68b717171c947",
                "code", code,
                "redirect_uri", "http://localhost:3000/login/kakao"
        );

        String token = kakaoOAuth2TokenFeignClient.findOAuth2Token(data)
                .map(KakaoOAuth2TokenResponse::accessToken)
                .orElse(null);

        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }

        return kakaoOAuth2FeignClient.findUserInfo("Bearer %s".formatted(token))
                .map(KakaoUserInfoResponse::kakaoAccount)
                .map(it -> OAuth2UserInfoDto.builder()
                        .email(it.email())
                        .thumbnail(it.thumbnail())
                        .build());
    }
}
