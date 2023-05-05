package com.yellowsunn.simpleboard.domain.posts.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleboard.api.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleboard.api.dto.posts.QPostsGetAllDto;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.yellowsunn.simpleboard.domain.comment.QComment.comment;
import static com.yellowsunn.simpleboard.domain.file.QFile.file;
import static com.yellowsunn.simpleboard.domain.postHit.QPostHit.postHit;
import static com.yellowsunn.simpleboard.domain.posts.QPosts.posts;
import static com.yellowsunn.simpleboard.userservice.domain.user.QUser.user;

public class PostsRepositoryCustomImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostsRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    @Override
    public Optional<Posts> findPost(Long id) {
        Posts findPost = queryFactory
                .selectFrom(posts)
                .leftJoin(posts.hit).fetchJoin()
                .where(posts.id.eq(id))
                .fetchFirst();

        return Optional.ofNullable(findPost);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostsGetAllDto> findDtoAll(Pageable pageable, String title, String username) {
        List<PostsGetAllDto> content = queryFactory
                .select(
                        new QPostsGetAllDto(posts.id, posts.type, posts.title, posts.createdDate,
                                user.username, postHit.hit,
                                select(comment.count()).from(comment).where(comment.post.eq(posts)),
                                select(file.count()).from(file).where(file.post.eq(posts))))
                .from(posts)
                .leftJoin(posts.hit, postHit)
                .where(containsTitle(title), containsUsername(username))
                .orderBy(posts.type.desc(), posts.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(posts)
                .where(containsTitle(title), containsUsername(username))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<LocalDateTime> findLastModifiedById(Long id) {
        LocalDateTime content = queryFactory.select(posts.lastModifiedDate)
                .from(posts)
                .where(posts.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    private BooleanExpression containsUsername(String username) {
        return StringUtils.hasText(username) ? user.username.contains(username) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return StringUtils.hasText(title) ? posts.title.contains(title) : null;
    }
}