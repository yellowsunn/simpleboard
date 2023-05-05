package com.yellowsunn.simpleboard.boardservice.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleboard.boardservice.domain.comment.QComment;
import com.yellowsunn.simpleboard.boardservice.domain.file.QFile;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.boardservice.domain.post.QPostHit;
import com.yellowsunn.simpleboard.boardservice.domain.post.QPosts;
import com.yellowsunn.simpleboard.boardservice.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleboard.boardservice.dto.posts.QPostsGetAllDto;
import com.yellowsunn.simpleboard.boardservice.repository.PostsRepository;
import com.yellowsunn.simpleboard.userservice.domain.user.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PostJpaRepository implements PostsRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public PostJpaRepository(EntityManager em) {
        this.entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Posts save(Posts entity) {
        Assert.notNull(entity, "Entity must not be null");

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Posts> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QPosts.posts)
                        .where(QPosts.posts.id.eq(id))
                        .fetchFirst()
        );
    }

    @Transactional
    @Override
    public Optional<Posts> findPost(Long id) {
        Posts findPost = queryFactory
                .selectFrom(QPosts.posts)
                .leftJoin(QPosts.posts.hit).fetchJoin()
                .where(QPosts.posts.id.eq(id))
                .fetchFirst();

        return Optional.ofNullable(findPost);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostsGetAllDto> findDtoAll(Pageable pageable, String title, String username) {
        List<PostsGetAllDto> content = queryFactory
                .select(
                        new QPostsGetAllDto(QPosts.posts.id, QPosts.posts.type, QPosts.posts.title, QPosts.posts.createdDate,
                                QUser.user.username, QPostHit.postHit.hit,
                                JPAExpressions.select(QComment.comment.count()).from(QComment.comment).where(QComment.comment.post.eq(QPosts.posts)),
                                JPAExpressions.select(QFile.file.count()).from(QFile.file).where(QFile.file.post.eq(QPosts.posts))))
                .from(QPosts.posts)
                .leftJoin(QPosts.posts.hit, QPostHit.postHit)
                .where(containsTitle(title), containsUsername(username))
                .orderBy(QPosts.posts.type.desc(), QPosts.posts.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(QPosts.posts)
                .where(containsTitle(title), containsUsername(username))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<LocalDateTime> findLastModifiedById(Long id) {
        LocalDateTime content = queryFactory.select(QPosts.posts.lastModifiedDate)
                .from(QPosts.posts)
                .where(QPosts.posts.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    @Override
    public void delete(Posts posts) {
        queryFactory.delete(QPosts.posts)
                .where(QPosts.posts.id.eq(posts.getId()))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    private BooleanExpression containsUsername(String username) {
        return StringUtils.hasText(username) ? QUser.user.username.contains(username) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return StringUtils.hasText(title) ? QPosts.posts.title.contains(title) : null;
    }
}
