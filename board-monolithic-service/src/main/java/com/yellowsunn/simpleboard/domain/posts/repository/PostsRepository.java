package com.yellowsunn.simpleboard.domain.posts.repository;

import com.yellowsunn.simpleboard.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long>, PostsRepositoryCustom {
}
