package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.dto.EmailLoginRequestDto;
import com.yellowsunn.userservice.dto.EmailSignUpRequestDto;
import com.yellowsunn.userservice.dto.OAuth2LinkUserRequestDto;
import com.yellowsunn.userservice.dto.OAuth2LoginOrSignUpRequestDto;
import com.yellowsunn.userservice.dto.OAuth2SignUpRequestDto;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpDto;
import com.yellowsunn.userservice.facade.UserAuthFacade;
import com.yellowsunn.userservice.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_UUID_HEADER;

@RequiredArgsConstructor
@RestController
public class ExternalUserAuthController {
    private final UserAuthFacade userAuthFacade;
    private final UserAuthService userAuthService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/email/signup")
    public boolean signUpEmail(@Valid @RequestBody EmailSignUpRequestDto requestDto) {
        var command = requestDto.toUserSignUpCommand();
        return userAuthFacade.signUpEmail(command);
    }

    @PostMapping("/api/v2/auth/email/login")
    public UserLoginDto loginEmail(@Valid @RequestBody EmailLoginRequestDto requestDto) {
        var command = requestDto.toUserLoginCommand();
        return userAuthService.loginEmail(command);
    }

    @PostMapping("/api/v2/auth/oauth2/login-signup")
    public UserOAuth2LoginOrSignUpDto loginOrSignUpOAuth2(@Valid @RequestBody OAuth2LoginOrSignUpRequestDto requestDto) {
        var command = requestDto.toCommand();
        return userAuthFacade.loginOrSignUpRequest(command);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v2/auth/oauth2/signup")
    public boolean singUpOAuth2(@Valid @RequestBody OAuth2SignUpRequestDto requestDto) {
        var command = requestDto.toUserOAuth2SignUpCommand();
        return userAuthFacade.signUpOAuth2(command);
    }

    @PutMapping("/api/v2/auth/oauth2/link")
    public boolean linkOAuth2(@RequestHeader(USER_UUID_HEADER) String userUUID,
                              @Valid @RequestBody OAuth2LinkUserRequestDto requestDto) {
        return userAuthService.linkOAuth2User(userUUID, requestDto.getTempUserToken());
    }
}
