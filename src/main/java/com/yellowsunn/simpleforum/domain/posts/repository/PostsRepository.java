package com.yellowsunn.simpleforum.domain.posts.repository;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long>, PostsRepositoryCustom {
}
