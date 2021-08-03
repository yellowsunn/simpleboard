package com.yellowsunn.simpleforum.domain.postHit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostHitRepository extends JpaRepository<PostHit, Long> {

    @Query("select ph.hit from PostHit ph where ph.post.id = :id")
    Optional<Long> findHitByPostId(@Param("id") Long id);
}
