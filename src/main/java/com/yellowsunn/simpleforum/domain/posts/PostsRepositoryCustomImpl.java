package com.yellowsunn.simpleforum.domain.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.yellowsunn.simpleforum.domain.posts.QPosts.posts;

public class PostsRepositoryCustomImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostsRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    @Override
    public Optional<Posts> findPostAndUpdateHit(Long id) {
        Posts findPost = queryFactory
                .selectFrom(posts)
                .leftJoin(posts.user).fetchJoin()
                .leftJoin(posts.hit).fetchJoin()
                .where(posts.id.eq(id))
                .fetchFirst();

        if (findPost != null) {
            findPost.updateHit();
        }

        return Optional.ofNullable(findPost);
    }

}
