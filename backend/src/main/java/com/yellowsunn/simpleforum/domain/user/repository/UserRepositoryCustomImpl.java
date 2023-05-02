package com.yellowsunn.simpleforum.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleforum.domain.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yellowsunn.simpleforum.domain.user.QUser.user;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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
