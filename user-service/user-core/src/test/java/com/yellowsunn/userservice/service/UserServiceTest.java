package com.yellowsunn.userservice.service;

import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import com.yellowsunn.userservice.repository.UserRepository;
import com.yellowsunn.userservice.utils.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserServiceTest {
    UserRepository userRepository = mock(UserRepository.class);
    UserProviderRepository userProviderRepository = mock(UserProviderRepository.class);

    UserService sut;

    @BeforeEach
    void setUp() {
        sut = new UserService(userRepository, userProviderRepository);
    }

    @Test
    void findUserInfo() {
        // given
        var uuid = "uuid";
        given(userRepository.findByUUID(uuid)).willReturn(Optional.of(getTestUser()));

        // when
        UserMyInfoDto userInfo = sut.findUserInfo(uuid);

        // then
        assertThat(userInfo.email()).isEqualTo("test@example.com");
    }

    @Test
    void findUserInfo_failed_when_user_not_found() {
        // given
        var uuid = "uuid";
        given(userRepository.findByUUID(uuid)).willReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> sut.findUserInfo(uuid));

        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUserInfo() {
        // given
        var uuid = "uuid";
        given(userRepository.findByUUID(uuid)).willReturn(Optional.of(getTestUser()));
        given(userRepository.delete(any(User.class))).willReturn(true);

        // when
        boolean isDeleted = sut.deleteUserInfo(uuid);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void deleteUserInfo_return_true_when_already_deleted() {
        // given
        var uuid = "uuid";
        given(userRepository.findByUUID(uuid)).willReturn(Optional.empty());

        // when
        boolean isDeleted = sut.deleteUserInfo(uuid);

        // then
        assertThat(isDeleted).isTrue();
    }

    @Test
    void changeUserThumbnail() {
        // given
        var uuid = "uuid";
        var user = getTestUser();
        var updatedThumbnail = "https://example.com/thubnail.png";
        given(userRepository.findByUUID(uuid)).willReturn(Optional.of(user));

        boolean isSuccess = sut.changeUserThumbnail(uuid, updatedThumbnail);

        assertThat(isSuccess).isTrue();
        assertThat(user.getThumbnail()).isEqualTo(updatedThumbnail);
    }

    private User getTestUser() {
        return User.emailUserBuilder()
                .email("test@example.com")
                .nickName("nickName")
                .password(new BCryptPasswordEncoder().encode("password"))
                .build();
    }
}
