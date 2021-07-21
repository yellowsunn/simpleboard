package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.security.encoder.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 성공")
    void login() {
        //given
        UserLoginDto dto = getTestUserLoginDto();
        Optional<User> userOptional = Optional.of(
                User.builder()
                        .username(dto.getUsername())
                        .password(dto.getPassword())
                        .nickname("nickname")
                        .build()
        );

        //mocking
        given(userRepository.findByUsername(dto.getUsername())).willReturn(userOptional);
        given(passwordEncoder.matches(dto.getPassword(), userOptional.get().getPassword())).willReturn(true);

        //then
        assertThatNoException().isThrownBy(() -> userService.login(dto));
    }

    @Test
    @DisplayName("로그인 요청시 아이디를 찾을 수 없는 경우 에러")
    void notFoundUsernameForLogin() {
        //given
        UserLoginDto dto = getTestUserLoginDto();

        //mocking
        given(userRepository.findByUsername(dto.getUsername()))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("로그인 요청시 비밀번호가 다른 경우 에러")
    void notSamePasswordForLogin() {
        //given
        UserLoginDto dto = getTestUserLoginDto();
        Optional<User> userOptional = Optional.of(
                User.builder()
                        .username(dto.getUsername())
                        .password("password2")
                        .nickname("nickname")
                        .build()
        );

        //mocking
        given(userRepository.findByUsername(dto.getUsername())).willReturn(userOptional);
        given(passwordEncoder.matches(dto.getPassword(), userOptional.get().getPassword())).willReturn(false);

        //then
        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private UserLoginDto getTestUserLoginDto() {
        return UserLoginDto.builder()
                .username("username")
                .password("password")
                .build();
    }
}