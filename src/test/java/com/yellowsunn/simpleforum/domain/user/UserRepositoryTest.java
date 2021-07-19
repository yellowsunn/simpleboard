package com.yellowsunn.simpleforum.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

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
                .role(Role.USER)
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
        assertThat(findUser.getRole()).isEqualTo(user.getRole());
    }
}