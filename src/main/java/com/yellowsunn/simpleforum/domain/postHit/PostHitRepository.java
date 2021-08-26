package com.yellowsunn.simpleforum.domain.postHit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostHitRepository extends JpaRepository<PostHit, Long> {

    Optional<PostHit> findByPostId(Long id);
}
