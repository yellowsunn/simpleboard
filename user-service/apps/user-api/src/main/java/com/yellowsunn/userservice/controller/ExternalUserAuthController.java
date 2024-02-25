package com.yellowsunn.userservice.controller;

import com.yellowsunn.common.annotation.LoginUser;
import com.yellowsunn.common.response.ResultResponse;
import com.yellowsunn.userservice.application.UserAuthFacade;
import com.yellowsunn.userservice.application.UserAuthService;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.controller.request.EmailLoginRequest;
import com.yellowsunn.userservice.controller.request.EmailSignUpRequest;
import com.yellowsunn.userservice.controller.request.OAuth2LinkUserRequest;
import com.yellowsunn.userservice.controller.request.OAuth2LoginOrSignUpRequest;
import com.yellowsunn.userservice.controller.request.OAuth2SignUpRequest;
import com.yellowsunn.userservice.controller.request.RefreshAccessTokenRequest;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpDto;
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

@RestController
@RequiredArgsConstructor
public class ExternalUserAuthController {

    private final UserAuthFacade userAuthFacade;
    private final UserAuthService userAuthService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/email/signup")
    public ResultResponse<Boolean> signUpEmail(@Valid @RequestBody EmailSignUpRequest request) {
        var command = request.toUserSignUpCommand();
        return ResultResponse.ok(
                userAuthService.signUpEmail(command)
        );
    }

    @PostMapping("/api/v2/auth/email/login")
    public ResultResponse<UserLoginTokenDto> loginEmail(@Valid @RequestBody EmailLoginRequest request) {
        var command = request.toUserLoginCommand();
        return ResultResponse.ok(
                userAuthService.loginEmail(command)
        );
    }

    @PostMapping("/api/v2/auth/oauth2/authorize")
    public ResultResponse<UserOAuth2LoginOrSignUpDto> loginOrSignUpOAuth2(
            @Valid @RequestBody OAuth2LoginOrSignUpRequest request
    ) {
        var command = request.toCommand();
        return ResultResponse.ok(
                userAuthFacade.loginOrSignUpRequest(command)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/oauth2/signup")
    public ResultResponse<UserLoginTokenDto> singUpCompleteOAuth2(
            @Valid @RequestBody OAuth2SignUpRequest request
    ) {
        var command = request.toUserOAuth2SignUpCommand();
        return ResultResponse.ok(
                userAuthFacade.signUpOAuth2(command)
        );
    }

    @PutMapping("/api/v2/auth/oauth2/link")
    public ResultResponse<Boolean> linkOAuth2(@LoginUser Long userId,
            @Valid @RequestBody OAuth2LinkUserRequest request
    ) {
        var command = request.toCommand(userId);
        return ResultResponse.ok(
                userAuthFacade.linkOAuth2User(command)
        );
    }

    @DeleteMapping("/api/v2/auth/oauth2/link")
    public ResultResponse<Boolean> unlinkOAuth2(
            @LoginUser Long userId,
            @RequestParam String type
    ) {
        var oAuth2Type = OAuth2Type.convertFrom(type);
        return ResultResponse.ok(
                userAuthService.unlinkOAuth2User(userId, oAuth2Type)
        );
    }

    @PostMapping("/api/v2/auth/token")
    public ResultResponse<UserLoginTokenDto> refreshUserToken(
            @Valid @RequestBody RefreshAccessTokenRequest request
    ) {
        return ResultResponse.ok(
                userAuthService.refreshUserToken(request.accessToken(), request.refreshToken())
        );
    }
}
