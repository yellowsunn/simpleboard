package com.yellowsunn.simpleboard.boardservice.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleboard.boardservice.domain.comment.Comment;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.boardservice.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.yellowsunn.simpleboard.boardservice.domain.comment.QComment.comment;

@Component
public class CommentJpaRepository implements CommentRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public CommentJpaRepository(EntityManager em) {
        this.entityManager = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Comment save(Comment entity) {
        Assert.notNull(entity, "Entity must not be null");

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(comment)
                        .where(comment.id.eq(id))
                        .fetchFirst()
        );
    }

    @Override
    public List<Comment> findByParent(Comment parent) {
        return queryFactory.selectFrom(comment)
                .where(comment.parent.eq(parent))
                .fetch();
    }

    @Override
    public List<Comment> findByPost(Posts post) {
        return queryFactory.selectFrom(comment)
                .where(comment.post.eq(post))
                .fetch();
    }

    @Override
    public Optional<Comment> findByIdQuery(Long id) {
        Comment findComment = queryFactory.selectFrom(comment)
                .where(comment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findComment);
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<Comment> findCursorBasedSliceByPostId(Long postId, String cursor, Pageable pageable) {
        List<Comment> content = queryFactory.selectFrom(comment)
                .where(comment.post.id.eq(postId), hasCursor(cursor))
                .orderBy(comment.parent.id.asc(), comment.id.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if (content.size() > pageable.getPageSize()) {
            return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
        } else {
            return new SliceImpl<>(content, pageable, false);
        }
    }

    @Override
    public void updateAllParentToNullInBatch(List<Comment> comments) {
        queryFactory.update(comment)
                .set(comment.parent, (Comment) null)
                .where(comment.in(comments))
                .execute();
    }

    @Override
    public void deleteAllInBatch(List<Comment> comments) {
        if (CollectionUtils.isEmpty(comments)) {
            return;
        }

        List<Long> commentIds = comments.stream().map(Comment::getId).toList();

        queryFactory.delete(comment)
                .where(comment.id.in(commentIds))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    private BooleanExpression hasCursor(String cursor) {
        return StringUtils.hasText(cursor) ?
                StringExpressions.lpad(comment.parent.id.stringValue(), 20, '0')
                        .concat(StringExpressions.lpad(comment.id.stringValue(), 20, '0')).gt(cursor) : null;
    }
}
