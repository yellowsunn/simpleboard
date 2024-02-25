package com.yellowsunn.userservice.infrastructure.persistence;

import static com.yellowsunn.userservice.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.application.port.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public boolean delete(User entity) {
        long execute = jpaQueryFactory.delete(user).execute();
        em.flush();
        return execute >= 0;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByNickName(String nickName) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(user)
                        .where(user.nickName.eq(nickName))
                        .fetchFirst()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByIds(List<Long> ids) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(user.id.in(ids))
                .fetch();
    }
}
