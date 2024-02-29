//package com.yellowsunn.userservice.persistence.user;
//
//import com.yellowsunn.userservice.domain.user.UserEntity;
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
//class UserJpaRepositoryTest extends PersistenceIntegrationTest {
//    @Autowired
//    TestEntityManager em;
//
//    UserJpaDeprecatedRepository userJpaRepository;
//
//    @BeforeEach
//    void setUp() {
//        userJpaRepository = new UserJpaDeprecatedRepository(em.getEntityManager());
//    }
//
//    @Test
//    void save() {
//        // given
//        UserEntity userEntity = UserEntity.emailUserBuilder()
//                .email("test@example.com")
//                .password("12345678")
//                .nickName("nickName")
//                .build();
//
//        // when
//        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
//
//        // then
//        assertThat(savedUserEntity.getId()).isPositive();
//    }
//
//    @Test
//    void findByEmailAndPassword() {
//        // given
//        UserEntity userEntity = UserEntity.emailUserBuilder()
//                .email("test2@example.com")
//                .password("12345678")
//                .nickName("test")
//                .build();
//        userJpaRepository.save(userEntity);
//
//        // when
//        Optional<UserEntity> findUser = userJpaRepository.findByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword());
//
//        // then
//        assertThat(findUser).isPresent();
//    }
//}
