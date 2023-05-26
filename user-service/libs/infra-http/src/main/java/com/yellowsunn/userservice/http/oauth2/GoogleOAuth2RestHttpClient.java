package com.yellowsunn.userservice.http.oauth2;

import com.yellowsunn.userservice.constant.OAuth2Request;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfo;
import com.yellowsunn.userservice.dto.GoogleOAuth2UserInfoResponseDto;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfoHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class GoogleOAuth2RestHttpClient implements OAuth2UserInfoHttpClient {
    private final RestTemplate restTemplate;

    private static final String BASE_URI = "https://oauth2.googleapis.com";

    public GoogleOAuth2RestHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2Type type() {
        return OAuth2Type.GOOGLE;
    }

    @Override
    public OAuth2UserInfo findUserInfo(String token, OAuth2Request oAuth2Request) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI)
                .path("/tokeninfo")
                .queryParam("id_token", token)
                .build()
                .toUri();

        var responseDto = restTemplate.getForObject(uri, GoogleOAuth2UserInfoResponseDto.class);
        Assert.notNull(responseDto, "Google oauth2 oAuth2Token info must not be null.");

        return new OAuth2UserInfo(responseDto.email(), responseDto.picture());
    }
}