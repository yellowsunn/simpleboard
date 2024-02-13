package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntity.create(user.toUserCreate());

        userJpaRepository.save(userEntity);
    }
}
