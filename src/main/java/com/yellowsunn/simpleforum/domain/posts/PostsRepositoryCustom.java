package com.yellowsunn.simpleforum.domain.posts;

import java.util.Optional;

public interface PostsRepositoryCustom {

    Optional<Posts> findByIdWithUser(Long id);
}