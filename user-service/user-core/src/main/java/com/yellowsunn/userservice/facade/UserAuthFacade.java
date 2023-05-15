package com.yellowsunn.userservice.facade;

import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpCommand;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpDto;
import com.yellowsunn.userservice.dto.UserOAuth2SignUpCommand;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.file.FileStorage;
import com.yellowsunn.userservice.http.OAuth2UserInfo;
import com.yellowsunn.userservice.http.client.OAuth2UserInfoHttpClientFactory;
import com.yellowsunn.userservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthFacade {
    private final UserAuthService userAuthService;
    private final OAuth2UserInfoHttpClientFactory oAuth2UserInfoHttpClientFactory;
    private final FileStorage fileStorage;

    public boolean signUpEmail(UserEmailSignUpCommand command) {
        String thumbnail = fileStorage.getDefaultThumbnail();
        return userAuthService.signUpEmail(command, thumbnail);
    }

    public boolean signUpOAuth2(UserOAuth2SignUpCommand command) {
        String thumbnail = fileStorage.getDefaultThumbnail();
        return userAuthService.signUpOAuth2(command, thumbnail);
    }

    public UserOAuth2LoginOrSignUpDto loginOrSignUpRequest(UserOAuth2LoginOrSignUpCommand command) {
        // OAuth2 유저 정보 조회
        var oAuth2UserInfoHttpClient = oAuth2UserInfoHttpClientFactory.get(command.type());
        OAuth2UserInfo userInfo = oAuth2UserInfoHttpClient.findUserInfo(command.oAuth2Token());
        verifyOAuth2ProviderEmailIsNotBlank(userInfo);

        // 로그인
        UserLoginDto userLoginDto = userAuthService.loginOAuth2(userInfo, command.type());
        if (userLoginDto != null) {
            return UserOAuth2LoginOrSignUpDto.loginBuilder()
                    .accessToken(userLoginDto.accessToken())
                    .refreshToken(userLoginDto.refreshToken())
                    .build();
        }

        // 회원정보를 찾을 수 없다면 회원가입을 위한 임시 데이터 생성 후 토큰값 반환
        String tempUserToken = userAuthService.saveTempOAuth2User(userInfo, command.type(), command.csrfToken());
        return UserOAuth2LoginOrSignUpDto.signUpRequestBuilder()
                .tempUserToken(tempUserToken)
                .build();
    }

    // OAuth2 계정 이메일을 조회할 수 없는 경우 예외 발생 (이메일 조회에 동의 하지 않은 경우)
    private static void verifyOAuth2ProviderEmailIsNotBlank(OAuth2UserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.email())) {
            throw new CustomUserException(UserErrorCode.NOT_FOUND_OAUTH_PROVIDER_EMAIL);
        }
    }
}
