package com.yellowsunn.simpleforum.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager em;

    @Test
    void saveAndFind() {
        //given
        User user = User.builder()
                .username("username")
                .password("password")
                .nickname("nickname")
                .build();

        userRepository.save(user);
        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findById(user.getId())
                .orElse(null);

        //then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
        assertThat(findUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("동일한 아이디로 중복 등록될 수 없다")
    void duplicateUsernameFail() {
        User user1 = User.builder().username("username").password("password").nickname("nickname").build();
        User user2 = User.builder().username("username").password("drowssap").nickname("emankcin").build();
        userRepository.saveAndFlush(user1);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}