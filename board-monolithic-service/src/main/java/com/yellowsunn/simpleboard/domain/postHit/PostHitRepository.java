package com.yellowsunn.simpleboard.domain.postHit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHitRepository extends JpaRepository<PostHit, Long> {

    Optional<PostHit> findByPostId(Long id);
}
