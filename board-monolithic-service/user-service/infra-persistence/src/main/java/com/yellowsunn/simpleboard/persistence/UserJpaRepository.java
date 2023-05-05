package com.yellowsunn.simpleboard.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import com.yellowsunn.simpleboard.userservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.yellowsunn.simpleboard.userservice.domain.user.QUser.user;

@Component
public class UserJpaRepository implements UserRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public UserJpaRepository(EntityManager em) {
        this.entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    @Override
    public User save(User entity) {
        Assert.notNull(entity, "Entity must not be null");

        entityManager.persist(entity);
        return entity;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(user.id.eq(aLong))
                        .fetchFirst()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(user.username.eq(username))
                        .fetchFirst()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<User> findCursorBasedSlice(String username, Long cursor, Pageable pageable) {
        List<User> content = queryFactory.selectFrom(user)
                .where(user.username.isNotNull(), hasCursor(cursor), startsWithUsername(username))
                .limit(pageable.getPageSize() + 1)
                .orderBy(user.id.asc())
                .fetch();

        if (content.size() > pageable.getPageSize()) {
            return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
        } else {
            return new SliceImpl<>(content, pageable, false);
        }
    }

    @Override
    public long findCursorBasedTotal(String username) {
        return queryFactory.selectFrom(user)
                .where(user.username.isNotNull(), startsWithUsername(username))
                .fetchCount();
    }

    private BooleanExpression startsWithUsername(String username) {
        return StringUtils.hasText(username) ? user.username.startsWith(username) : null;
    }

    private BooleanExpression hasCursor(Long cursor) {
        return cursor != null ? user.id.gt(cursor) : null;
    }
}
