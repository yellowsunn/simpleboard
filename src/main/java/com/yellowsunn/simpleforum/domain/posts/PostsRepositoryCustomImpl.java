package com.yellowsunn.simpleforum.domain.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleforum.api.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleforum.api.dto.posts.QPostsGetAllDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.yellowsunn.simpleforum.domain.comment.QComment.comment;
import static com.yellowsunn.simpleforum.domain.file.QFile.file;
import static com.yellowsunn.simpleforum.domain.postHit.QPostHit.postHit;
import static com.yellowsunn.simpleforum.domain.posts.QPosts.posts;
import static com.yellowsunn.simpleforum.domain.user.QUser.user;

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

    @Transactional(readOnly = true)
    @Override
    public Page<PostsGetAllDto> findDtoAll(Pageable pageable) {
        List<PostsGetAllDto> content = queryFactory
                .select(
                        new QPostsGetAllDto(posts.id, posts.type, posts.title, posts.createdDate,
                                user.username, postHit.hit,
                                select(comment.count()).from(comment),
                                select(file.count()).from(file)))
                .from(posts)
                .leftJoin(posts.hit, postHit)
                .leftJoin(posts.user, user)
                .orderBy(posts.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(posts).fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
