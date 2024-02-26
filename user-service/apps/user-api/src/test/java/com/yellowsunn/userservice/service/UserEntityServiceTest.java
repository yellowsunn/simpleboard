package com.yellowsunn.userservice.service;

import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.userservice.application.UserService;
import com.yellowsunn.userservice.domain.user.UserEntity;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.utils.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserEntityServiceTest {
    UserDeprecatedRepository userDeprecatedRepository = mock(UserDeprecatedRepository.class);
    UserProviderDeprecatedRepository userProviderDeprecatedRepository = mock(UserProviderDeprecatedRepository.class);

    UserService sut;

    @BeforeEach
    void setUp() {
        sut = new UserService(userDeprecatedRepository, userProviderDeprecatedRepository);
    }

    @Test
    void findUserInfo() {
        // given
        var userId = 1L;
        given(userDeprecatedRepository.findById(userId)).willReturn(Optional.of(getTestUser()));

        // when
        UserMyInfoDto userInfo = sut.findUserInfo(userId);

        // then
        assertThat(userInfo.email()).isEqualTo("test@example.com");
    }

    @Test
    void findUserInfo_failed_when_user_not_found() {
        // given
        var userId = 1L;
        given(userDeprecatedRepository.findById(userId)).willReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> sut.findUserInfo(userId));

        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUserInfo() {
        // given
        var userId = 1L;
        given(userDeprecatedRepository.findById(userId)).willReturn(Optional.of(getTestUser()));
        given(userDeprecatedRepository.delete(any(UserEntity.class))).willReturn(true);

        // when
        boolean isDeleted = sut.deleteUserInfo(userId);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void deleteUserInfo_return_true_when_already_deleted() {
        // given
        var userId = 1L;
        given(userDeprecatedRepository.findById(userId)).willReturn(Optional.empty());

        // when
        boolean isDeleted = sut.deleteUserInfo(userId);

        // then
        assertThat(isDeleted).isTrue();
    }

    @Test
    void changeUserThumbnail() {
        // given
        var userId = 1L;
        var user = getTestUser();
        var updatedThumbnail = "https://example.com/thubnail.png";
        given(userDeprecatedRepository.findById(1L)).willReturn(Optional.of(user));

        boolean isSuccess = sut.changeUserThumbnail(userId, updatedThumbnail);

        assertThat(isSuccess).isTrue();
        assertThat(user.getThumbnail()).isEqualTo(updatedThumbnail);
    }

    private UserEntity getTestUser() {
        return UserEntity.emailUserBuilder()
                .email("test@example.com")
                .nickName("nickName")
                .password(new BCryptPasswordEncoder().encode("password"))
                .build();
    }
}
