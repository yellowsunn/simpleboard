package com.yellowsunn.userservice.infrastructure.http.feignclient;

import com.yellowsunn.userservice.infrastructure.http.response.GoogleUserInfoResponse;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleOAuth2FeignClient", url = "https://oauth2.googleapis.com")
public interface GoogleOAuth2FeignClient {

    @GetMapping("/tokeninfo")
    Optional<GoogleUserInfoResponse> findGoogleUserInfo(@RequestParam("id_token") String token);
}
