package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.User;
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
        Posts post = postsUploadDto.covertToPosts(user);
        return postsRepository.save(post);
    }
}
