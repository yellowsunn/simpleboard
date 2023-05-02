package com.yellowsunn.simpleboard.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellowsunn.simpleboard.domain.comment.Comment;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.StringExpressions.lpad;
import static com.yellowsunn.simpleboard.domain.comment.QComment.comment;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Comment> findByIdQuery(Long id) {
        Comment findComment = queryFactory.selectFrom(comment)
                .leftJoin(comment.user).fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findComment);
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<Comment> findCursorBasedSliceByPostId(Long postId, String cursor, Pageable pageable) {
        List<Comment> content = queryFactory.selectFrom(comment)
                .leftJoin(comment.user).fetchJoin()
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

    private BooleanExpression hasCursor(String cursor) {
        return StringUtils.hasText(cursor) ?
                lpad(comment.parent.id.stringValue(), 20, '0')
                        .concat(lpad(comment.id.stringValue(), 20, '0')).gt(cursor) : null;
    }
}
