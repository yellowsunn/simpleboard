package com.yellowsunn.simpleboard.domain.comment.repository;

import com.yellowsunn.simpleboard.domain.comment.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<Comment> findByIdQuery(Long id);

    Slice<Comment> findCursorBasedSliceByPostId(Long postId, String cursor, Pageable pageable);

    void updateAllParentToNullInBatch(List<Comment> comments);
}
