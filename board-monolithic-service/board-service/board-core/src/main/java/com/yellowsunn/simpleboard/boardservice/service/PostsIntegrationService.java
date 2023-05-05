package com.yellowsunn.simpleboard.boardservice.service;

import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.boardservice.dto.posts.PostsEditDto;
import com.yellowsunn.simpleboard.boardservice.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleboard.boardservice.util.FileStore;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import com.yellowsunn.simpleboard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostsIntegrationService {

    private final UserService userService;
    private final PostsService postsService;
    private final FileService fileService;
    private final FileStore fileStore;

    @Transactional
    public Long upload(Long userId, PostsUploadDto postsUploadDto) throws IOException {
        User user = userService.findUser(userId);
        Posts post = postsService.save(user, postsUploadDto);
        fileService.storeAndSaveAll(post, fileStore.storeFiles(postsUploadDto.getImageFiles()));

        return post.getId();
    }

    @Transactional
    public void edit(Long postId, Long userId, PostsEditDto postsEditDto) throws IOException {
        Posts post = postsService.edit(postId, userId, postsEditDto);
        fileService.storeAndSaveAll(post, fileStore.storeFiles(postsEditDto.getImageFiles()));
    }
}
