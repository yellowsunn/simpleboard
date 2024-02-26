package com.yellowsunn.userservice.infrastructure.persistence;

import static com.yellowsunn.userservice.domain.user.QUserEntity.userEntity;
import static com.yellowsunn.userservice.domain.user.QUserProviderEntity.userProviderEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserEntity;
import com.yellowsunn.userservice.domain.user.UserProviderEntity;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepository userJpaRepository;
    private final UserProviderJpaRepository userProviderJpaRepository;

    public UserRepositoryImpl(
            EntityManager entityManager,
            UserJpaRepository userJpaRepository,
            UserProviderJpaRepository userProviderJpaRepository
    ) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.userJpaRepository = userJpaRepository;
        this.userProviderJpaRepository = userProviderJpaRepository;
    }

    @Transactional
    @Override
    public void insert(User user) {
        UserEntity userEntity = UserEntity.create(user);
        userJpaRepository.save(userEntity);

        List<UserProviderEntity> userProviderEntities = UserProviderEntity.creates(user.getUserId(),
                user.getUserProviders());
        userProviderJpaRepository.saveAll(userProviderEntities);
    }

    @Transactional
    @Override
    public void update(User user) {
        UserEntity userEntity = userJpaRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("Not found user."));
        userEntity.update(user);

        List<UserProviderEntity> userProviderEntities = userProviderJpaRepository.findByUserId(user.getUserId());

        List<UserProvider> prevUserProviders = userProviderEntities.stream()
                .map(UserProviderEntity::toUserProvider)
                .toList();

        List<UserProvider> newUserProviders = ListUtils.removeAll(user.getUserProviders(), prevUserProviders);

        userProviderJpaRepository.saveAll(UserProviderEntity.creates(user.getUserId(), newUserProviders));

        List<UserProvider> deletedUserProviders = ListUtils.removeAll(prevUserProviders, user.getUserProviders());
        deletedUserProviders.forEach(it -> deleteUserProviderEntity(user.getUserId(), it.getProvider(), it.getEmail()));
    }

    @Transactional
    @Override
    public void delete(User user) {
        List<UserProviderEntity> userProviderEntities = userProviderJpaRepository.findByUserId(user.getUserId());
        userProviderJpaRepository.deleteAll(userProviderEntities);

        userJpaRepository.findByUserId(user.getUserId())
                .ifPresent(userJpaRepository::delete);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmailAndProvider(String email, Provider provider) {
        Optional<UserProviderEntity> userProviderEntityOptional = userProviderJpaRepository.findByProviderAndProviderEmail(
                provider, email);

        if (userProviderEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        UserProviderEntity userProviderEntity = userProviderEntityOptional.get();

        return userJpaRepository.findByUserId(userProviderEntity.getUserId())
                .map(it -> it.toUser(List.of(userProviderEntity)));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId)
                .map(userEntity -> {
                    var userProviderEntities = userProviderJpaRepository.findByUserId(userId)
                            .stream()
                            .filter(userProviderEntity ->
                                    StringUtils.equals(userEntity.getUserId(), userProviderEntity.getUserId())
                            ).toList();
                    return userEntity.toUser(userProviderEntities);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByNickName(String nickName) {
        return userJpaRepository.findByNickName(nickName)
                .map(userEntity -> {
                    List<UserProviderEntity> userProviderEntities = userProviderJpaRepository.findByUserId(
                            userEntity.getUserId());
                    return userEntity.toUser(userProviderEntities);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByUserIds(List<String> userIds) {
        List<UserProviderEntity> userProviderEntities = userProviderJpaRepository.findByUserIdIn(userIds);
        List<UserEntity> userEntities = userJpaRepository.findByUserIdIn(userIds);

        return userEntities.stream()
                .map(userEntity -> {
                    List<UserProviderEntity> filteredUserProviderEntities = userProviderEntities.stream()
                            .filter(userProviderEntity ->
                                    StringUtils.equals(userEntity.getUserId(), userProviderEntity.getUserId())
                            ).toList();
                    return userEntity.toUser(filteredUserProviderEntities);
                }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public User getByUserId(String userId) {
        return userJpaRepository.findByUserId(userId)
                .map(userEntity -> {
                    var userProviderEntities = userProviderJpaRepository.findByUserId(userId)
                            .stream()
                            .filter(userProviderEntity ->
                                    StringUtils.equals(userEntity.getUserId(), userProviderEntity.getUserId())
                            ).toList();
                    return userEntity.toUser(userProviderEntities);
                }).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmailAndProvider(String email, Provider provider) {
        Long count = jpaQueryFactory
                .select(userProviderEntity.count())
                .from(userProviderEntity)
                .where(
                        userProviderEntity.providerEmail.eq(email),
                        userProviderEntity.provider.eq(provider)
                ).fetchFirst();

        return count != null && count >= 1;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByNickName(String nickName) {
        Long count = jpaQueryFactory
                .select(userEntity.count())
                .from(userEntity)
                .where(userEntity.nickName.eq(nickName))
                .fetchFirst();

        return count != null && count >= 1;
    }

    private void deleteUserProviderEntity(String userId, Provider provider, String email) {
        userProviderJpaRepository.findByProviderAndProviderEmail(provider, email)
                .filter(it -> StringUtils.equals(it.getUserId(), userId))
                .ifPresent(userProviderJpaRepository::delete);
    }
}
