package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Posts save(User user, PostsUploadDto postsUploadDto) throws IOException {
        checkAuthorityForType(user, postsUploadDto.getType());
        Posts post = postsUploadDto.covertToPosts(user);
        return postsRepository.save(post);
    }

    @Transactional
    public PostsGetDto findById(Long id) {
        return postsRepository.findPostAndUpdateHit(id)
                .map(PostsGetDto::new)
                .orElseThrow(NotFoundUserException::new);
    }

    private void checkAuthorityForType(User user, PostType type) {
        if (user.getRole() != Role.ADMIN && type == PostType.NOTICE) {
            throw new ForbiddenException();
        }
    }
}
