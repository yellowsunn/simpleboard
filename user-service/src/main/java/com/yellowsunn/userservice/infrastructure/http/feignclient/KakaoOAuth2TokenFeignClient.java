package com.yellowsunn.userservice.infrastructure.http.feignclient;

import com.yellowsunn.userservice.infrastructure.http.response.KakaoOAuth2TokenResponse;
import java.util.Map;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "KakaoOAuth2TokenFeignClient", url = "https://kauth.kakao.com")
public interface KakaoOAuth2TokenFeignClient {

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Optional<KakaoOAuth2TokenResponse> findOAuth2Token(Map<String, ?> data);
}
