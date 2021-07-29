package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.file.FileUploadDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.api.util.FileStore;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsIntegrationService {

    private final UserService userService;
    private final PostsService postsService;
    private final FileService fileService;
    private final FileStore fileStore;

    @Transactional
    public void upload(Long userId, PostsUploadDto postsUploadDto) throws IOException {
        User user = userService.findUser(userId);
        Posts post = postsService.save(user, postsUploadDto);
        storeAndSaveAllFiles(post, postsUploadDto);
    }

    private void storeAndSaveAllFiles(Posts post, PostsUploadDto postsUploadDto) throws IOException {
        List<FileUploadDto> dtos = fileStore.storeFiles(postsUploadDto.getImageFiles());
        fileService.saveAll(post, dtos);
    }
}
