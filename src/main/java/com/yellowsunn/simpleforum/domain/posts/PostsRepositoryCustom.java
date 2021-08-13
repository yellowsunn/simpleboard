package com.yellowsunn.simpleforum.domain.posts;

import com.yellowsunn.simpleforum.api.dto.posts.PostsGetAllDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostsRepositoryCustom {

    Optional<Posts> findPostAndUpdateHit(Long id);

    Page<PostsGetAllDto> findDtoAll(Pageable pageable, String title, String username);
}
