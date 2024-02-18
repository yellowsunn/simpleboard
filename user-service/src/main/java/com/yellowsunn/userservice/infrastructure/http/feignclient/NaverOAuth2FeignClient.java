package com.yellowsunn.userservice.infrastructure.http.feignclient;

import com.yellowsunn.userservice.infrastructure.http.response.NaverUserInfoResponse;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "NaverOAuth2FeignClient", url = "https://openapi.naver.com")
public interface NaverOAuth2FeignClient {

    @GetMapping("/v1/nid/me")
    Optional<NaverUserInfoResponse> findUserInfo(@RequestHeader("Authorization") String bearerAuth);
}
