package com.yellowsunn.simpleboard.domain.comment.repository;

import com.yellowsunn.simpleboard.domain.comment.Comment;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByParent(Comment parent);

    List<Comment> findByPost(Posts post);
}
