package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserEntity;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserProviderEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProviderJpaRepository userProviderJpaRepository;

    @Transactional
    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntity.create(user.toUserCreate());

        userJpaRepository.save(userEntity);

        List<UserProviderEntity> userProviderEntities = UserProviderEntity.creates(user.toUserProviderCreates());

        userProviderJpaRepository.saveAll(userProviderEntities);
    }
}
