package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.domain.postHit.PostHit;
import com.yellowsunn.simpleforum.domain.postHit.PostHitRepository;
import com.yellowsunn.simpleforum.exception.NotFoundException;
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
