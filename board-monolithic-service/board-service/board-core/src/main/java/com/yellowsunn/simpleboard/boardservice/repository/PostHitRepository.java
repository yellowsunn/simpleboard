package com.yellowsunn.simpleboard.boardservice.repository;


import com.yellowsunn.simpleboard.boardservice.domain.post.PostHit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHitRepository extends JpaRepository<PostHit, Long> {

    Optional<PostHit> findByPostId(Long id);
}
