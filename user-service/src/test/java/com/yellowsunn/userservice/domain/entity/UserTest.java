package com.yellowsunn.userservice.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void userId는_UUID_형식이_아니면_예외_발생() {
        // given
        String userId = "aaaa";
        String email = "test@example.com";
        String nickname = "test";
        String password = "password";

        // when
        // then
        assertThatThrownBy(() -> User.createEmailUser(userId, email, nickname, password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이메일_사용자를_생성할_수_있다() {
        // given
        String userId = UUID.randomUUID().toString();
        String email = "test@example.com";
        String nickname = "test";
        String password = "password";

        // when
        User emailUser = User.createEmailUser(userId, email, nickname, password);

        // then
        assertThat(emailUser).isNotNull();
    }
}
