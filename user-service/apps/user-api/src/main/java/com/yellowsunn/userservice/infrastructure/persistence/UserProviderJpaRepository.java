package com.yellowsunn.userservice.infrastructure.persistence;

import static com.yellowsunn.userservice.domain.user.QUserProvider.userProvider;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.application.port.UserProviderRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Repository
public class UserProviderJpaRepository implements UserProviderRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    public UserProviderJpaRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Transactional
    @Override
    public UserProvider save(UserProvider entity) {
        Assert.notNull(entity, "User entity must not be null.");
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public boolean existsByUserIdAndProvider(Long userId, Provider provider) {
        Long count = jpaQueryFactory
                .select(userProvider.id.count())
                .from(userProvider)
                .where(
                        userProvider.user.id.eq(userId),
                        userProvider.provider.eq(provider)
                ).fetchFirst();

        return count != null && count > 0;
    }

    @Override
    public Optional<UserProvider> findByProviderEmailAndProvider(String providerEmail, Provider provider) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(userProvider)
                        .where(
                                userProvider.providerEmail.eq(providerEmail),
                                userProvider.provider.eq(provider)
                        ).leftJoin(userProvider.user).fetchJoin()
                        .fetchFirst()
        );
    }

    @Override
    public List<Provider> findProvidersByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return jpaQueryFactory
                .select(userProvider.provider)
                .from(userProvider)
                .where(userProvider.user.id.eq(userId))
                .fetch();
    }

    @Override
    public boolean deleteByUserIdAndProvider(Long userId, Provider provider) {
        long execute = jpaQueryFactory
                .delete(userProvider)
                .where(userProvider.user.id.eq(userId), userProvider.provider.eq(provider))
                .execute();
        return execute >= 0;
    }

    @Override
    public long countProvidersByUserId(Long userId) {
        Long count = jpaQueryFactory
                .select(userProvider.id.count())
                .from(userProvider)
                .where(userProvider.user.id.eq(userId))
                .fetchFirst();

        return count != null ? count : 0L;
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        long count = jpaQueryFactory
                .delete(userProvider)
                .where(userProvider.user.id.eq(userId))
                .execute();

        return count >= 0;
    }
}
