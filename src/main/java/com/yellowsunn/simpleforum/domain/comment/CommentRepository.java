package com.yellowsunn.simpleforum.domain.comment;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Query(value = "select c from Comment c left join fetch c.user where c.post.id=:postId " +
            "order by coalesce(c.parent.id, c.id) asc, c.id asc",
            countQuery = "select count(c) from Comment c where c.post.id=:postId")
    Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);
}
