package com.yellowsunn.userservice.presentation.controller;

import com.yellowsunn.userservice.application.UserEmailService;
import com.yellowsunn.userservice.application.UserOAuth2Service;
import com.yellowsunn.userservice.domain.dto.OAuth2UserInfoDto;
import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.vo.UserId;
import com.yellowsunn.userservice.presentation.request.EmailLoginRequest;
import com.yellowsunn.userservice.presentation.request.EmailSignUpRequest;
import com.yellowsunn.userservice.presentation.request.GoogleUserInfoRequest;
import com.yellowsunn.userservice.presentation.request.KakaoUserInfoRequest;
import com.yellowsunn.userservice.presentation.request.NaverUserInfoRequest;
import com.yellowsunn.userservice.utils.UuidHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserEmailService userEmailService;
    private final UserOAuth2Service userOAuth2Service;
    private final UuidHolder uuidHolder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v3/auth/email/signup")
    public String emailSignUp(@RequestBody EmailSignUpRequest request) {
        UserId userId = UserId.fromString(uuidHolder.random());
        UserCreateCommand command = request.toUserCreateCommand(userId);

        userEmailService.createEmailUser(command);

        return userId.toString();
    }

    @PostMapping("/api/v3/auth/email/login")
    public String emailLogin(@RequestBody EmailLoginRequest request) {
        UserId userId = userEmailService.login(request.email(), request.password());

        return userId.toString();
    }

    // TODO: 회원정보를 가져오고 로그인까지 완료해야 함
    @PostMapping("/api/v2/auth/oauth2/google")
    public OAuth2UserInfoDto getGoogleUserInfo(@RequestBody GoogleUserInfoRequest request) {

        return userOAuth2Service.getGoogleUserInfo(request.token());
    }

    // TODO: 회원정보를 가져오고 로그인까지 완료해야 함
    @PostMapping("/api/v2/auth/oauth2/naver")
    public OAuth2UserInfoDto getNaverUserInfo(@RequestBody NaverUserInfoRequest request) {

        return userOAuth2Service.getNaverUserInfo(request.token());
    }

    // TODO: 회원정보를 가져오고 로그인까지 완료해야 함
    @PostMapping("/api/v2/auth/oauth2/kakao")
    public OAuth2UserInfoDto getKakaoUserInfo(@RequestBody KakaoUserInfoRequest request) {

        OAuth2UserInfoDto kakaoToken = userOAuth2Service.getKakaoToken(request.token());
        return kakaoToken;
    }
}
