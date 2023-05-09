package com.yellowsunn.userservice.service;

import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.dto.UserEmailSignUpCommand;
import com.yellowsunn.userservice.dto.UserLoginCommand;
import com.yellowsunn.userservice.dto.UserLoginDto;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.BCryptPasswordEncoder;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import com.yellowsunn.userservice.utils.token.AccessTokenGenerator;
import com.yellowsunn.userservice.utils.token.RefreshTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserEmailAuthServiceTest {
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserEmailAuthService sut;

    @BeforeEach
    void setUp() {
        var accessTokenGenerator = new AccessTokenGenerator(UUID.randomUUID().toString(), Duration.ofSeconds(5L));
        var refreshTokenGenerator = new RefreshTokenGenerator(UUID.randomUUID().toString(), Duration.ofSeconds(10L));
        sut = new UserEmailAuthService(
                userRepository,
                passwordEncoder,
                accessTokenGenerator,
                refreshTokenGenerator
        );
    }

    @Test
    void signUp() {
        // given
        var email = "test@example.com";
        var nickName = "test";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").nickName(nickName).build();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(i -> i.getArguments()[0]);
        given(userRepository.existsByNickName(nickName)).willReturn(false);

        // when
        boolean isSuccess = sut.signUp(command, "defaultThumbnail");

        // then
        assertThat(isSuccess).isTrue();
    }

    @Test
    void signUp_failed_when_already_exist_email() {
        // given
        var email = "test@example.com";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));

        // when
        Throwable throwable = catchThrowable(() -> sut.signUp(command, "defaultThumbnail"));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.ALREADY_EXIST_EMAIL);
    }

    @Test
    void signUp_failed_when_already_exist_nickName() {
        // given
        var email = "test@example.com";
        var nickName = "test";
        var command = UserEmailSignUpCommand.builder().email(email).password("password").nickName(nickName).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(userRepository.existsByNickName(nickName)).willReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> sut.signUp(command, "defaultThumbnail"));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.ALREADY_EXIST_NICKNAME);
    }

    @Test
    void login() {
        var email = "test@example.com";
        var password = "password";
        var command = UserLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));

        UserLoginDto userLoginDto = sut.login(command);

        assertThat(userLoginDto.accessToken()).isNotBlank();
        assertThat(userLoginDto.refreshToken()).isNotBlank();
    }

    @Test
    void login_failed_when_password_mismatch() {
        // given
        var email = "test@example.com";
        var password = "otherpassword";
        var command = UserLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(getTestUser()));

        // when
        Throwable throwable = catchThrowable(() -> sut.login(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.INVALID_LOGIN);
    }

    @Test
    void login_failed_when_not_found_email() {
        // given
        var email = "test@example.com";
        var password = "password";
        var command = UserLoginCommand.builder().email(email).password(password).build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> sut.login(command));

        // then
        assertThat(throwable).isInstanceOf(CustomUserException.class);
        assertThat(((CustomUserException) throwable).getErrorCode()).isEqualTo(UserErrorCode.INVALID_LOGIN);
    }

    private User getTestUser() {
        return User.builder()
                .email("test@example.com")
                .nickName("nickName")
                .password(passwordEncoder.encode("password"))
                .build();
    }
}
