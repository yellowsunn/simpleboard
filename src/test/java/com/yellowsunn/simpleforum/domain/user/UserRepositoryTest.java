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
        User user = getTestUser();
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
        User user1 = getTestUser();
        User user2 = User.builder().username("username").password("drowssap").nickname("emankcin").build();
        userRepository.saveAndFlush(user1);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findByUsername() {
        //given
        User user = getTestUser();
        userRepository.saveAndFlush(user);
        em.clear();

        //when
        User findUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        //then
        assertThat(findUser).isNotNull();
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getUsername()).isEqualTo(findUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());
    }

    @Test
    void failedFindByUsername() {
        //given
        User user = getTestUser();
        userRepository.saveAndFlush(user);
        em.flush();

        //then
        assertThat(userRepository.findByUsername("test").isEmpty()).isTrue();
    }

    private User getTestUser() {
        return User.builder()
                .username("username")
                .password("password")
                .nickname("nickname")
                .build();
    }

}