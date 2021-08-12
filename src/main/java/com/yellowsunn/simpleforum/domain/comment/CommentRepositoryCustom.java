package com.yellowsunn.simpleforum.domain.comment;

import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<Comment> findByIdQuery(Long id);

    void deleteAllByParentIdQuery(Long parentId);
}
