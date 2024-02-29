//package com.yellowsunn.userservice.persistence.user;
//
//import com.yellowsunn.userservice.domain.user.Provider;
//import com.yellowsunn.userservice.domain.user.UserEntity;
//import com.yellowsunn.userservice.domain.user.UserProviderEntity;
//import com.yellowsunn.userservice.persistence.PersistenceIntegrationTest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class UserProviderJpaRepositoryTest extends PersistenceIntegrationTest {
//    @Autowired
//    TestEntityManager em;
//
//    UserJpaDeprecatedRepository userJpaRepository;
//
//    UserProviderJpaDeprecatedRepository userProviderJpaRepository;
//
//    @BeforeEach
//    void setUp() {
//        userProviderJpaRepository = new UserProviderJpaDeprecatedRepository(em.getEntityManager());
//        userJpaRepository = new UserJpaDeprecatedRepository(em.getEntityManager());
//    }
//
//    @Test
//    void existsByUserIdAndProvider() {
//        // given
//        UserEntity userEntity = generateTestUser();
//        UserProviderEntity userProviderEntity = generateTestUserProvider(userEntity);
//
//        // when
//        boolean isExist = userProviderJpaRepository.existsByUserIdAndProvider(userEntity.getId(), userProviderEntity.getProvider());
//
//        // then
//        assertThat(isExist).isTrue();
//    }
//
//    @Test
//    void findByProviderEmailAndProvider() {
//        // given
//        UserEntity userEntity = generateTestUser();
//        UserProviderEntity userProviderEntity = generateTestUserProvider(userEntity);
//
//        // when
//        Optional<UserProviderEntity> foundUserProviderOptional = userProviderJpaRepository.findByProviderEmailAndProvider(
//                userProviderEntity.getProviderEmail(), userProviderEntity.getProvider());
//
//        // then
//        assertThat(foundUserProviderOptional).isPresent();
//    }
//
//    private UserEntity generateTestUser() {
//        var user = UserEntity.emailUserBuilder()
//                .nickName("nickName")
//                .email("test@example.com")
//                .password("password")
//                .build();
//        return userJpaRepository.save(user);
//    }
//
//    private UserProviderEntity generateTestUserProvider(UserEntity userEntity) {
//        UserProviderEntity userProviderEntity = UserProviderEntity.builder()
//                .provider(Provider.EMAIL)
//                .providerEmail("test@example.com")
//                .user(userEntity)
//                .build();
//
//        return userProviderJpaRepository.save(userProviderEntity);
//    }
//}
