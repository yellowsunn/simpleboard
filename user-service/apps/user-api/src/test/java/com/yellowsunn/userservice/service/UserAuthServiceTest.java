package com.yellowsunn.userservice.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import com.yellowsunn.common.utils.token.AccessTokenHandler;
import com.yellowsunn.common.utils.token.RefreshTokenHandler;
import com.yellowsunn.userservice.application.UserAuthService;
import com.yellowsunn.userservice.application.command.UserEmailLoginCommand;
import com.yellowsunn.userservice.application.command.UserEmailSignUpCommand;
import com.yellowsunn.userservice.application.port.TempUserCacheRepository;
import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.mock.FakeUserRepository;
import com.yellowsunn.userservice.utils.BCryptPasswordEncoder;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAuthServiceTest {

    TempUserCacheRepository tempUserCacheRepository = mock(TempUserCacheRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserRepository userRepository;

    UserAuthService sut;

    @BeforeEach
    void setUp() {
        var accessTokenGenerator = new AccessTokenHandler(UUID.randomUUID().toString(), Duration.ofSeconds(5L));
        var refreshTokenGenerator = new RefreshTokenHandler(UUID.randomUUID().toString(), Duration.ofSeconds(10L));
        userRepository = new FakeUserRepository();
        sut = new UserAuthService(
                tempUserCacheRepository,
                passwordEncoder,
                accessTokenGenerator,
                refreshTokenGenerator,
                userRepository
        );
    }

    @Test
    void 이메일_회원가입_성공() {
        // given
        var command = UserEmailSignUpCommand.builder()
                .email("test@example.com")
                .password("password")
                .nickName("test")
                .build();

        // when
        // then
        assertDoesNotThrow(() -> sut.signUpEmail(command));
    }

    @Test
    void 증복된_이메일로__회원가입_시도할_경우_예외발생() {
        // given
        userRepository.insert(
                User.createEmailUser(UUID.randomUUID().toString(), "test@example.com", "nickname", "password")
        );

        var email = "test@example.com";
        var command = UserEmailSignUpCommand.builder()
                .email(email)
                .password("password")
                .build();

        // when
        // then
        assertThatThrownBy(() -> sut.signUpEmail(command))
                .isInstanceOf(CustomUserException.class);
    }

    @Test
    void 중복된_닉네임으로_이메일_회원가입_시도할_경우_예외발생() {
        // given
        var nickname = "test";
        userRepository.insert(
                User.createEmailUser(UUID.randomUUID().toString(), "test@example.com", nickname, "password")
        );

        var command = UserEmailSignUpCommand.builder()
                .email("test2@example.com")
                .password("password")
                .nickName(nickname)
                .build();

        // when
        // then
        assertThatThrownBy(() -> sut.signUpEmail(command))
                .isInstanceOf(CustomUserException.class);
    }

    @Test
    void 로그인_시_패스워드가_일치하지_않는_경우_예외_발생() {
        // given
        var email = "test@example.com";
        var password = "otherpassword";

        userRepository.insert(
                User.createEmailUser(UUID.randomUUID().toString(), "test@example.com", "nickname", "password")
        );

        var command = UserEmailLoginCommand.builder()
                .email(email)
                .password(password)
                .build();

        // when
        // then
        assertThatThrownBy(() -> sut.loginEmail(command))
                .isInstanceOf(CustomUserException.class);
    }

    @Test
    void 이메일을_찾을_수_없는_경우_예외_발생() {
        // given
        var email = "test@example.com";
        var password = "password";
        var command = UserEmailLoginCommand.builder()
                .email(email)
                .password(password)
                .build();

        // when
        // then
        assertThatThrownBy(() -> sut.loginEmail(command))
                .isInstanceOf(CustomUserException.class);
    }
//
//    @Test
//    void loginOAuth2() {
//        // given
//        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
//        var google = Provider.GOOGLE;
//        var userProvider = UserProviderEntity.builder()
//                .providerEmail(oAuth2UserInfo.email())
//                .provider(google)
//                .user(getTestUser())
//                .build();
//        given(userProviderDeprecatedRepository.findByProviderEmailAndProvider(oAuth2UserInfo.email(), google))
//                .willReturn(Optional.of(userProvider));
//
//        // when
//        var userLoginDto = sut.loginOAuth2(oAuth2UserInfo, OAuth2Type.GOOGLE);
//
//        // then
//        assertThat(userLoginDto.accessToken()).isNotBlank();
//        assertThat(userLoginDto.refreshToken()).isNotBlank();
//    }
//
//    @Test
//    void saveTempOAuth2User() {
//        // given
//        var oAuth2UserInfo = new OAuth2UserInfo("test@example.com", "https://example.com/thumbnail.png");
//        var csrfToken = "12345678";
//
//        // when
//        String tempUserToken = sut.saveTempOAuth2User(oAuth2UserInfo, OAuth2Type.GOOGLE, csrfToken);
//
//        // then
//        assertThat(tempUserToken).isNotBlank();
//    }
//
//    private UserEntity getTestUser() {
//        return UserEntity.emailUserBuilder()
//                .email("test@example.com")
//                .nickName("nickName")
//                .password(passwordEncoder.encode("password"))
//                .build();
//    }
}
