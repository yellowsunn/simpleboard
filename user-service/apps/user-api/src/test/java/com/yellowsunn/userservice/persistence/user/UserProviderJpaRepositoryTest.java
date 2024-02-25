package com.yellowsunn.userservice.persistence.user;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.infrastructure.persistence.UserJpaRepository;
import com.yellowsunn.userservice.infrastructure.persistence.UserProviderJpaRepository;
import com.yellowsunn.userservice.persistence.PersistenceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserProviderJpaRepositoryTest extends PersistenceIntegrationTest {
    @Autowired
    TestEntityManager em;

    UserJpaRepository userJpaRepository;

    UserProviderJpaRepository userProviderJpaRepository;

    @BeforeEach
    void setUp() {
        userProviderJpaRepository = new UserProviderJpaRepository(em.getEntityManager());
        userJpaRepository = new UserJpaRepository(em.getEntityManager());
    }

    @Test
    void existsByUserIdAndProvider() {
        // given
        User user = generateTestUser();
        UserProvider userProvider = generateTestUserProvider(user);

        // when
        boolean isExist = userProviderJpaRepository.existsByUserIdAndProvider(user.getId(), userProvider.getProvider());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    void findByProviderEmailAndProvider() {
        // given
        User user = generateTestUser();
        UserProvider userProvider = generateTestUserProvider(user);

        // when
        Optional<UserProvider> foundUserProviderOptional = userProviderJpaRepository.findByProviderEmailAndProvider(userProvider.getProviderEmail(), userProvider.getProvider());

        // then
        assertThat(foundUserProviderOptional).isPresent();
    }

    private User generateTestUser() {
        var user = User.emailUserBuilder()
                .nickName("nickName")
                .email("test@example.com")
                .password("password")
                .build();
        return userJpaRepository.save(user);
    }

    private UserProvider generateTestUserProvider(User user) {
        UserProvider userProvider = UserProvider.builder()
                .provider(Provider.EMAIL)
                .providerEmail("test@example.com")
                .user(user)
                .build();

        return userProviderJpaRepository.save(userProvider);
    }
}
