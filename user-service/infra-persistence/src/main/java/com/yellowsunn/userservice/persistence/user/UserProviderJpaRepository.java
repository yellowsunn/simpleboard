package com.yellowsunn.userservice.persistence.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserProvider;
import com.yellowsunn.userservice.repository.UserProviderRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import static com.yellowsunn.userservice.domain.user.QUserProvider.userProvider;

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
}
