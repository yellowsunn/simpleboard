package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.user.UserGetDto;
import com.yellowsunn.simpleforum.api.dto.user.UserLoginDto;
import com.yellowsunn.simpleforum.api.dto.user.UserPatchRequestDto;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import com.yellowsunn.simpleforum.exception.PasswordMismatchException;
import com.yellowsunn.simpleforum.security.encoder.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    User mockUser;

    Long userId = 1L;

    @Test
    @DisplayName("로그인 성공")
    void login() {
        //given
        UserLoginDto dto = getTestUserLoginDto();
        Optional<User> userOptional = Optional.of(
                User.builder()
                        .username(dto.getUsername())
                        .password(dto.getPassword())
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
                .isInstanceOf(NotFoundException.class);
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
                        .build()
        );

        //mocking
        given(userRepository.findByUsername(dto.getUsername())).willReturn(userOptional);
        given(passwordEncoder.matches(dto.getPassword(), userOptional.get().getPassword())).willReturn(false);

        //then
        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("id로 유저 조회")
    void findUserById() {
        //given
        User user = getTestUser();
        UserGetDto userDto = new UserGetDto(user);

        //mocking
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        UserGetDto findUserDto = userService.findUserById(1L);

        //then
        assertThat(findUserDto).isNotNull();
        assertThat(userDto).isEqualTo(findUserDto);
    }

    @Test
    @DisplayName("id로 유저 조회 실패시 에러")
    void failedToFindUserById() {
        //mocking
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> userService.findUserById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword() {
        //given
        User user = getTestUser();
        Long userId = 1L;
        UserPatchRequestDto userDto = new UserPatchRequestDto();
        userDto.setPassword(user.getPassword());
        userDto.setNewPassword("password2");

        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(userDto.getNewPassword())).willReturn(userDto.getNewPassword());

        //when
        userService.changePassword(userId, userDto);

        //then
        assertThat(user.getPassword()).isEqualTo(userDto.getNewPassword());
    }

    @Test
    @DisplayName("비밀번호 변경 요청시 기존 비밀번호 입력이 일치하지 않는 경우 에러")
    void invalidOldPassword() {
        //given
        User user = getTestUser();
        Long userId = 1L;
        UserPatchRequestDto userDto = new UserPatchRequestDto();
        userDto.setPassword("passssss");
        userDto.setNewPassword("password2");

        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).willReturn(false);

        //when
        assertThatThrownBy(() -> userService.changePassword(userId, userDto))
                .isInstanceOf(PasswordMismatchException.class);

        //then
        assertThat(user.getPassword()).isNotEqualTo(userDto.getNewPassword());
    }

    @Test
    @DisplayName("현재 로그인되어 있는 회원 삭제 성공")
    void deleteCurrentUser() {
        //given
        User user = getTestUser();
        Long userId = 1L;

        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //then
        assertThatNoException().isThrownBy(() -> userService.deleteCurrentUser(userId));
    }

    @Test
    @DisplayName("삭제하려는 회원 조회 실패")
    void failedToFindDeleteUser() {
        //given
        Long userId = 1L;

        //mocking
        given(userRepository.findById(userId)).willThrow(NotFoundException.class);

        //then
        assertThatThrownBy(() -> userService.deleteCurrentUser(userId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("아이디로 회원 삭제 성공")
    void deleteById() {
        //mocking
        given(mockUser.getRole()).willReturn(Role.USER);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        //then
        assertThatNoException().isThrownBy(() -> userService.deleteById(userId));
    }

    @Test
    @DisplayName("아이디로 회원 삭제 실패 에러 - 관리자는 삭제할 수 없다")
    void failedToDeleteById() {
        //mocking
        given(mockUser.getRole()).willReturn(Role.ADMIN);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        //then
        assertThatThrownBy(() -> userService.deleteById(userId))
                .isInstanceOf(ForbiddenException.class);
    }

    UserLoginDto getTestUserLoginDto() {
        return UserLoginDto.builder()
                .username("username")
                .password("password")
                .build();
    }

    User getTestUser() {
        return User.builder()
                .username("username")
                .password("password")
                .build();
    }
}