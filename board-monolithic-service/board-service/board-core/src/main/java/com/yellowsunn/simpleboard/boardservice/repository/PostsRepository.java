package com.yellowsunn.simpleboard.boardservice.repository;


import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.boardservice.dto.posts.PostsGetAllDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostsRepository {
    Posts save(Posts entity);

    Optional<Posts> findById(Long id);

    Optional<Posts> findPost(Long id);

    Page<PostsGetAllDto> findDtoAll(Pageable pageable, String title, String username);

    Optional<LocalDateTime> findLastModifiedById(Long id);

    void delete(Posts posts);
}
