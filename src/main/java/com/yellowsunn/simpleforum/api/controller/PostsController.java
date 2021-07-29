package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.argumentresolver.LoginId;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.api.service.PostsIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsIntegrationService postsIntegrationService;

    @PostMapping
    public void upload(@LoginId Long userId,
                       @ModelAttribute PostsUploadDto postsUploadDto) throws IOException {
        postsIntegrationService.upload(userId, postsUploadDto);
    }
}
