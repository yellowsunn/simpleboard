package com.yellowsunn.userservice.infrastructure.http.feignclient;

import com.yellowsunn.userservice.infrastructure.http.response.KakaoUserInfoResponse;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoOAuth2FeignClient", url = "https://kapi.kakao.com")
public interface KakaoOAuth2FeignClient {

    @GetMapping("/v2/user/me")
    Optional<KakaoUserInfoResponse> findUserInfo(@RequestHeader("Authorization") String bearerAuth);
}
