package com.yellowsunn.simpleboard.domain.posts.repository;

import com.yellowsunn.simpleboard.api.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostsRepositoryCustom {

    Optional<Posts> findPost(Long id);

    Page<PostsGetAllDto> findDtoAll(Pageable pageable, String title, String username);

    Optional<LocalDateTime> findLastModifiedById(Long id);
}
