package com.yellowsunn.userservice.persistence.user;

import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.infrastructure.persistence.UserJpaRepository;
import com.yellowsunn.userservice.persistence.PersistenceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserJpaRepositoryTest extends PersistenceIntegrationTest {
    @Autowired
    TestEntityManager em;

    UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        userJpaRepository = new UserJpaRepository(em.getEntityManager());
    }

    @Test
    void save() {
        // given
        User user = User.emailUserBuilder()
                .email("test@example.com")
                .password("12345678")
                .nickName("nickName")
                .build();

        // when
        User savedUser = userJpaRepository.save(user);

        // then
        assertThat(savedUser.getId()).isPositive();
    }

    @Test
    void findByEmailAndPassword() {
        // given
        User user = User.emailUserBuilder()
                .email("test2@example.com")
                .password("12345678")
                .nickName("test")
                .build();
        userJpaRepository.save(user);

        // when
        Optional<User> findUser = userJpaRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        // then
        assertThat(findUser).isPresent();
    }
}
