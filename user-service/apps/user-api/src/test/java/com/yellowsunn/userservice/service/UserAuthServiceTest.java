package com.yellowsunn.userservice.service;

import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.dto.UserEmailLoginCommand;
import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginTokenDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfo;
import com.yellowsunn.userservice.repository.TempUserCacheRepository;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.BCryptPasswordEncoder;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserAuthServiceTest {
    UserRepository userRepository = mock(UserRepository.class);
    UserProviderRepository userProviderRepository = mock(UserProviderRepository.class);
    TempUserCacheRepository tempUserCacheRepository = mock(TempUserCacheRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserAuthService sut;

    @BeforeEach
    void setUp() {
        var accessTokenGenerator = new AccessTokenHandler(UUID.randomUUID().toString(), Duration.ofSeconds(5L));
        var refreshTokenGenerator = new RefreshTokenHandler(UUID.randomUUID().toString(), Duration.ofSeconds(10L));
        sut = new UserAuthService(
                userRepository,
                userProviderRepository,
                tempUserCacheRepository,
                passwordEncoder,
                accessTokenGenerator,
                refreshTokenGenerator
        );
    }

    @Test
    void signUpEmail() {
        // given
        var email = "test@example.com";
        var nickName = "test";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").nickName(nickName).build();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(i -> i.getArguments()[0]);
        given(userRepository.existsByNickName(nickName)).willReturn(false);
        given(userProviderRepository.save(any(UserProvider.class))).willAnswer(i -> i.getArguments()[0]);

        // when
        boolean isSuccess = sut.signUpEmail(command);

        // then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void signUpEmail_failed_when_already_exist_email() {
        // given
        var email = "test@example.com";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));

        // when
        Throwable throwable = catchThrowable(() -> sut.signUpEmail(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.ALREADY_EXIST_EMAIL);
    }

    @Test
    void signUpEmail_failed_when_already_exist_nickName() {
        // given
        var email = "test@example.com";
        var nickName = "test";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").nickName(nickName).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(userRepository.existsByNickName(nickName)).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> sut.signUpEmail(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.ALREADY_EXIST_NICKNAME);
    }

    @Test
    void loginEmail() {
        var email = "test@example.com";
        var password = "password";
        var command = UserEmailLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));
        given(userProviderRepository.existsByUserIdAndProvider(any(), eq(Provider.EMAIL))).willReturn(true);

        UserLoginTokenDto userLoginTokenDto = sut.loginEmail(command);

        assertThat(userLoginTokenDto.accessToken()).isNotBlank();
        assertThat(userLoginTokenDto.refreshToken()).isNotBlank();
    }

    @Test
    void loginEmail_failed_when_password_mismatch() {
        // given
        var email = "test@example.com";
        var password = "otherpassword";
        var command = UserEmailLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));
        given(userProviderRepository.existsByUserIdAndProvider(any(), eq(Provider.EMAIL))).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> sut.loginEmail(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.INVALID_LOGIN);
    }

    @Test
    void loginEmail_failed_when_not_found_email() {
        // given
        var email = "test@example.com";
        var password = "password";
        var command = UserEmailLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> sut.loginEmail(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.INVALID_LOGIN);
    }

    @Test
    void loginOAuth2() {
        // given
        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
        var google = Provider.GOOGLE;
        var userProvider = UserProvider.builder()
                .providerEmail(oAuth2UserInfo.email())
                .provider(google)
                .user(getTestUser())
                .build();
        given(userProviderRepository.findByProviderEmailAndProvider(oAuth2UserInfo.email(), google))
                .willReturn(Optional.of(userProvider));

        // when
        var userLoginDto = sut.loginOAuth2(oAuth2UserInfo, OAuth2Type.GOOGLE);

        // then
        assertThat(userLoginDto.accessToken()).isNotBlank();
        assertThat(userLoginDto.refreshToken()).isNotBlank();
    }

    @Test
    void saveTempOAuth2User() {
        // given
        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
        var csrfToken = "12345678";

        // when
        String tempUserToken = sut.saveTempOAuth2User(oAuth2UserInfo, OAuth2Type.GOOGLE, csrfToken);

        // then
        assertThat(tempUserToken).isNotBlank();
    }

    private User getTestUser() {
        return User.emailUserBuilder()
                .email("test@example.com")
                .nickName("nickName")
                .password(passwordEncoder.encode("password"))
                .build();
    }
}
