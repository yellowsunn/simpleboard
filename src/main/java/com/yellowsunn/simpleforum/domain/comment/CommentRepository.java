package com.yellowsunn.simpleforum.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Long countByPostId(Long postId);
}
