package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.argumentresolver.LoginId;
import com.yellowsunn.simpleforum.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.api.service.PostsIntegrationService;
import com.yellowsunn.simpleforum.api.service.PostsService;
import com.yellowsunn.simpleforum.domain.postHit.PostHitRepository;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsIntegrationService postsIntegrationService;
    private final PostsService postsService;
    private final PostHitRepository postHitRepository;

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
    public ResponseEntity<PostsGetDto> getPost(@PathVariable Long id) {
        PostsGetDto postsGetDto = postsService.findPost(id);
        return getNoCachePostsGetDtoEntity(postsGetDto);
    }

    @GetMapping("/{id}/hit")
    public Long getPostHit(@PathVariable Long id) {
        return postHitRepository.findHitByPostId(id)
                .orElseThrow(NotFoundException::new);
    }

    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @LoginId Long userId,
                     @Validated @ModelAttribute PostsEditDto postsEditDto,
                     BindingResult bindingResult) {
        checkValidation(bindingResult);
        postsService.edit(id, userId, postsEditDto);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation error");
        }
    }

    private ResponseEntity<PostsGetDto> getNoCachePostsGetDtoEntity(PostsGetDto postsGetDto) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .lastModified(postsGetDto.getLastModifiedDate().atZone(ZoneId.of("UTC")))
                .body(postsGetDto);
    }
}
