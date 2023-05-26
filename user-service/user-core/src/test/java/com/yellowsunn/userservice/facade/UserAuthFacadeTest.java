package com.yellowsunn.userservice.facade;

import com.yellowsunn.userservice.constant.OAuth2Request;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.dto.UserOAuth2LoginOrSignUpCommand;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfo;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfoHttpClient;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfoHttpClientFactory;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import com.yellowsunn.userservice.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserAuthFacadeTest {
    UserAuthService userAuthService = mock(UserAuthService.class);
    OAuth2UserInfoHttpClient oAuth2UserInfoHttpClient = mock(OAuth2UserInfoHttpClient.class);
    TempUserCacheRepository tempUserCacheRepository = mock(TempUserCacheRepository.class);

    UserAuthFacade sut;

    @BeforeEach
    void setUp() {
        given(oAuth2UserInfoHttpClient.type()).willReturn(OAuth2Type.GOOGLE);
        sut = new UserAuthFacade(
                userAuthService,
                new OAuth2UserInfoHttpClientFactory(List.of(oAuth2UserInfoHttpClient)),
                tempUserCacheRepository
        );
    }

    @DisplayName("OAuth2 로그인 성공")
    @Test
    void loginOrSignUpRequest_success_login() {
        // given
        var command = UserOAuth2LoginOrSignUpCommand.builder()
                .oAuth2Token("12345678")
                .csrfToken("2345678")
                .type(OAuth2Type.GOOGLE)
                .build();
        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
        var userLoginDto = UserLoginTokenDto.builder()
                .accessToken("access-oAuth2Token")
                .refreshToken("refresh-oAuth2Token")
                .build();
        given(oAuth2UserInfoHttpClient.findUserInfo(command.oAuth2Token(), OAuth2Request.LOGIN)).willReturn(oAuth2UserInfo);
        given(userAuthService.loginOAuth2(oAuth2UserInfo, OAuth2Type.GOOGLE)).willReturn(userLoginDto);

        // when
        var userOAuth2LoginOrSignUpDto = sut.loginOrSignUpRequest(command);

        // then
        assertThat(userOAuth2LoginOrSignUpDto.getIsLogin()).isTrue();
    }

    @DisplayName("OAuth2 로그인 시 회원 정보를 찾을 수 없으면 임시 회원 정보 저장 후 토큰 값 반환")
    @Test
    void loginOrSignUpRequest_failed_to_login_then_save_temp_user_info() {
        // given
        var command = UserOAuth2LoginOrSignUpCommand.builder()
                .oAuth2Token("12345678")
                .csrfToken("2345678")
                .type(OAuth2Type.GOOGLE)
                .build();
        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
        given(oAuth2UserInfoHttpClient.findUserInfo(command.oAuth2Token(), OAuth2Request.LOGIN)).willReturn(oAuth2UserInfo);
        given(userAuthService.loginOAuth2(oAuth2UserInfo, OAuth2Type.GOOGLE)).willReturn(null);
        given(userAuthService.saveTempOAuth2User(oAuth2UserInfo, OAuth2Type.GOOGLE, command.csrfToken()))
                .willReturn("temp-user-oAuth2Token");

        // when
        var userOAuth2LoginOrSignUpDto = sut.loginOrSignUpRequest(command);

        // then
        assertThat(userOAuth2LoginOrSignUpDto.getIsLogin()).isFalse();
        assertThat(userOAuth2LoginOrSignUpDto.getTempUserToken()).isEqualTo("temp-user-oAuth2Token");
    }

    @DisplayName("OAuth2 서버에서 회원의 이메일을 조회할 수 없으면 에러 반환 (이메일 제공에 동의하지 않은 경우)")
    @Test
    void loginOrSignUpRequest_failed_to_not_found_email() {
        // given
        var command = UserOAuth2LoginOrSignUpCommand.builder()
                .oAuth2Token("12345678")
                .csrfToken("2345678")
                .type(OAuth2Type.GOOGLE)
                .build();
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfo(null, null);
        given(oAuth2UserInfoHttpClient.findUserInfo(command.oAuth2Token(), OAuth2Request.LOGIN)).willReturn(oAuth2UserInfo);

        // when
        Throwable throwable = catchThrowable(() -> sut.loginOrSignUpRequest(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
    }
}
