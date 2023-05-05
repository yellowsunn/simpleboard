package com.yellowsunn.simpleboard.boardservice.service;

import com.yellowsunn.common.exception.NotFoundException;
import com.yellowsunn.simpleboard.boardservice.domain.post.PostHit;
import com.yellowsunn.simpleboard.boardservice.repository.PostHitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostHitService {

    private final PostHitRepository postHitRepository;

    @Transactional
    public Long findAndUpdatePostHit(Long postId) {
        PostHit postHit = postHitRepository.findByPostId(postId)
                .orElseThrow(NotFoundException::new);

        postHit.updateHit();

        return postHit.getHit();
    }
}
