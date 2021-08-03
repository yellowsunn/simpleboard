package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.argumentresolver.LoginId;
import com.yellowsunn.simpleforum.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.api.service.PostsIntegrationService;
import com.yellowsunn.simpleforum.api.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsIntegrationService postsIntegrationService;
    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<Void> upload(@LoginId Long userId,
                                 @Validated @ModelAttribute PostsUploadDto postsUploadDto,
                                 BindingResult bindingResult) throws IOException {
        checkValidation(bindingResult);
        Long postId = postsIntegrationService.upload(userId, postsUploadDto);

        return ResponseEntity.created(URI.create("/api/posts/" + postId))
                .body(null);
    }

    @GetMapping("/{id}")
    public PostsGetDto getPost(@PathVariable Long id) {
        return postsService.findById(id);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation error");
        }
    }
}
