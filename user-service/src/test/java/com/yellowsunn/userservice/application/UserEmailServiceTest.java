package com.yellowsunn.userservice.application;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.yellowsunn.userservice.domain.dto.UserCreateCommand;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import com.yellowsunn.userservice.mock.FakeUserRepository;
import com.yellowsunn.userservice.mock.TestPasswordEncoder;
import com.yellowsunn.userservice.utils.PasswordEncoder;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEmailServiceTest {

    UserEmailService userEmailService;

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        passwordEncoder = new TestPasswordEncoder("encrypted");
        userEmailService = UserEmailService.builder()
                .userRepository(userRepository)
                .passwordEncoder(passwordEncoder)
                .build();
    }

    @Test
    void 이메일_유저_생성() {
        // given
        UserCreateCommand command = UserCreateCommand.builder()
                .userId(UUID.randomUUID().toString())
                .email("test@example.com")
                .nickname("test")
                .password("password")
                .build();

        // when
        assertThatNoException()
                .isThrownBy(() -> userEmailService.createEmailUser(command));
    }
}
