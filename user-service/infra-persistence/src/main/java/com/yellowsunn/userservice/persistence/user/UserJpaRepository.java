package com.yellowsunn.userservice.persistence.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import static com.yellowsunn.userservice.domain.user.QUser.user;

@Repository
public class UserJpaRepository implements UserRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    public UserJpaRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Transactional
    @Override
    public User save(User entity) {
        Assert.notNull(entity, "User entity must not be null.");
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(user)
                        .where(user.email.eq(email), user.password.eq(password))
                        .fetchFirst()
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(user)
                        .where(user.email.eq(email))
                        .fetchFirst()
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(user)
                        .where(user.id.eq(id))
                        .fetchFirst()
        );
    }

    @Override
    public boolean delete(User entity) {
        long execute = jpaQueryFactory.delete(user).execute();
        em.flush();
        return execute >= 0;
    }

    @Override
    public boolean existsByNickName(String nickName) {
        Long count = jpaQueryFactory.select(user.id.count())
                .from(user)
                .where(user.nickName.eq(nickName))
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    public Optional<User> findByUUID(String uuid) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(user)
                        .where(user.uuid.eq(uuid))
                        .fetchFirst()
        );
    }
}
