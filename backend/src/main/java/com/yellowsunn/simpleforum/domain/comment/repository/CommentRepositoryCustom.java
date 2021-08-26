package com.yellowsunn.simpleforum.domain.comment.repository;

import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<Comment> findByIdQuery(Long id);

    Slice<Comment> findCursorBasedSliceByPostId(Long postId, String cursor, Pageable pageable);

    void deleteAllByParentIdQuery(Long parentId);

    void deleteAllByPostQuery(Posts post);
}
