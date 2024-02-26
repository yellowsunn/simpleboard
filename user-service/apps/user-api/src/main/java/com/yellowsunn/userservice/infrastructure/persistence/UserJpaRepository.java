package com.yellowsunn.userservice.infrastructure.persistence;

import com.yellowsunn.userservice.domain.user.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);

    List<UserEntity> findByUserIdIn(List<String> userIds);

    Optional<UserEntity> findByNickName(String nickName);
}
