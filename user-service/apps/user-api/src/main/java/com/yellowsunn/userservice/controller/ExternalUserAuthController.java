package com.yellowsunn.userservice.controller;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.response.ResultResponse;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.dto.EmailLoginRequestDto;
import com.yellowsunn.userservice.dto.EmailSignUpRequestDto;
import com.yellowsunn.userservice.dto.OAuth2LinkUserRequestDto;
import com.yellowsunn.userservice.dto.OAuth2LoginOrSignUpRequestDto;
import com.yellowsunn.userservice.dto.OAuth2SignUpRequestDto;
import com.yellowsunn.userservice.dto.RefreshAccessTokenRequestDto;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpDto;
import com.yellowsunn.userservice.facade.UserAuthFacade;
import com.yellowsunn.userservice.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ExternalUserAuthController {
    private final UserAuthFacade userAuthFacade;
    private final UserAuthService userAuthService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/email/signup")
    public ResultResponse<Boolean> signUpEmail(@Valid @RequestBody EmailSignUpRequestDto requestDto) {
        var command = requestDto.toUserSignUpCommand();
        return ResultResponse.ok(
                userAuthService.signUpEmail(command)
        );
    }

    @PostMapping("/api/v2/auth/email/login")
    public ResultResponse<UserLoginTokenDto> loginEmail(@Valid @RequestBody EmailLoginRequestDto requestDto) {
        var command = requestDto.toUserLoginCommand();
        return ResultResponse.ok(
                userAuthService.loginEmail(command)
        );
    }

    @PostMapping("/api/v2/auth/oauth2/authorize")
    public ResultResponse<UserOAuth2LoginOrSignUpDto> loginOrSignUpOAuth2(@Valid @RequestBody OAuth2LoginOrSignUpRequestDto requestDto) {
        var command = requestDto.toCommand();
        return ResultResponse.ok(
                userAuthFacade.loginOrSignUpRequest(command)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/oauth2/signup")
    public ResultResponse<UserLoginTokenDto> singUpCompleteOAuth2(@Valid @RequestBody OAuth2SignUpRequestDto requestDto) {
        var command = requestDto.toUserOAuth2SignUpCommand();
        return ResultResponse.ok(
                userAuthFacade.signUpOAuth2(command)
        );
    }

    @PutMapping("/api/v2/auth/oauth2/link")
    public ResultResponse<Boolean> linkOAuth2(@LoginUser Long userId,
                                              @Valid @RequestBody OAuth2LinkUserRequestDto requestDto) {
        var command = requestDto.toCommand(userId);
        return ResultResponse.ok(
                userAuthFacade.linkOAuth2User(command)
        );
    }

    @DeleteMapping("/api/v2/auth/oauth2/link")
    public ResultResponse<Boolean> unlinkOAuth2(@LoginUser Long userId,
                                                @RequestParam String type) {
        var oAuth2Type = OAuth2Type.convertFrom(type);
        return ResultResponse.ok(
                userAuthService.unlinkOAuth2User(userId, oAuth2Type)
        );
    }

    @PostMapping("/api/v2/auth/token")
    public ResultResponse<UserLoginTokenDto> refreshUserToken(@Valid @RequestBody RefreshAccessTokenRequestDto requestDto) {
        return ResultResponse.ok(
                userAuthService.refreshUserToken(requestDto.accessToken(), requestDto.refreshToken())
        );
    }
}
