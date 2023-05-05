package com.yellowsunn.simpleboard.api.service;

import com.yellowsunn.simpleboard.domain.postHit.PostHit;
import com.yellowsunn.simpleboard.domain.postHit.PostHitRepository;
import com.yellowsunn.common.exception.NotFoundException;
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
