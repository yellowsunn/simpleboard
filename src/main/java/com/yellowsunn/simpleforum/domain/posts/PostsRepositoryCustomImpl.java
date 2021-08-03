package com.yellowsunn.simpleforum.domain.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.yellowsunn.simpleforum.domain.posts.QPosts.posts;

public class PostsRepositoryCustomImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostsRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Posts> findByIdWithUser(Long id) {
        Posts findPost = queryFactory
                .selectFrom(posts)
                .join(posts.user).fetchJoin()
                .where(posts.id.eq(id))
                .fetchFirst();

        return Optional.ofNullable(findPost);
    }

}
