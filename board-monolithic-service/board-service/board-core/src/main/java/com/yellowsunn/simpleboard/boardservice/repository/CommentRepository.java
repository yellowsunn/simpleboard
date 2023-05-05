package com.yellowsunn.simpleboard.boardservice.repository;

import com.yellowsunn.simpleboard.boardservice.domain.comment.Comment;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment entity);

    Optional<Comment> findById(Long id);

    List<Comment> findByParent(Comment parent);

    List<Comment> findByPost(Posts post);

    Optional<Comment> findByIdQuery(Long id);

    Slice<Comment> findCursorBasedSliceByPostId(Long postId, String cursor, Pageable pageable);

    void updateAllParentToNullInBatch(List<Comment> comments);

    void deleteAllInBatch(List<Comment> comments);
}
