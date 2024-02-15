package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.domain.dto.EmailUserInfoDto;
import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.application.port.out.UserRepository;
import com.yellowsunn.userservice.domain.vo.Provider;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserEntity;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserProviderEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Override
    public Optional<EmailUserInfoDto> findEmailUserInfo(String email) {

        return userProviderJpaRepository.findByEmailAndProvider(email, Provider.EMAIL)
                .filter(userProviderEntity -> userJpaRepository.existsByUserId(userProviderEntity.getUserId()))
                .map(userProviderEntity -> EmailUserInfoDto.builder()
                        .userId(userProviderEntity.getUserId())
                        .password(userProviderEntity.getPassword())
                        .build()
                );
    }
}
