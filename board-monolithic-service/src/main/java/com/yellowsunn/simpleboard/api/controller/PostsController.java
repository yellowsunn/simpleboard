package com.yellowsunn.simpleboard.api.controller;

import com.yellowsunn.common.annotation.LoginId;
import com.yellowsunn.simpleboard.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleboard.api.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleboard.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleboard.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleboard.api.service.PostHitService;
import com.yellowsunn.simpleboard.api.service.PostsIntegrationService;
import com.yellowsunn.simpleboard.api.service.PostsService;
import com.yellowsunn.simpleboard.api.util.RefererFilter;
import com.yellowsunn.simpleboard.domain.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final PostHitService postHitService;
    private final PostsRepository postsRepository;
    private final RefererFilter refererFilter;

    @PostMapping
    public ResponseEntity<Void> upload(@LoginId Long userId,
                                 @Validated @ModelAttribute PostsUploadDto postsUploadDto,
                                 BindingResult bindingResult) throws IOException {
        refererFilter.check("/posts/write");
        checkValidation(bindingResult);
        Long postId = postsIntegrationService.upload(userId, postsUploadDto);

        return ResponseEntity.created(URI.create("/api/posts/" + postId))
                .body(null);
    }

    @GetMapping
    public Page<PostsGetAllDto> getPosts(Pageable pageable,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) String username) {
        return postsRepository.findDtoAll(pageable, title, username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsGetDto> getPost(@PathVariable Long id,
                                               @RequestHeader(name = HttpHeaders.IF_MODIFIED_SINCE, required = false) String ifModifiedSince) {
        if (postsService.hasNotModified(id, ifModifiedSince)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        PostsGetDto postsGetDto = postsService.findPost(id);
        return getNoCachePostsGetDtoEntity(postsGetDto);
    }

    @GetMapping("/{id}/hit")
    public Long getPostHit(@PathVariable Long id) {
        return postHitService.findAndUpdatePostHit(id);
    }

    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @LoginId Long userId,
                     @Validated @ModelAttribute PostsEditDto postsEditDto,
                     BindingResult bindingResult) throws IOException {
        refererFilter.check("/posts/" + id + "/edit");
        checkValidation(bindingResult);
        postsIntegrationService.edit(id, userId, postsEditDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @LoginId Long userId) {
        refererFilter.check("/posts/" + id);
        postsService.delete(id, userId);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }

    private ResponseEntity<PostsGetDto> getNoCachePostsGetDtoEntity(PostsGetDto postsGetDto) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .lastModified(postsGetDto.getLastModifiedDate().atZone(ZoneId.of("UTC")))
                .body(postsGetDto);
    }
}
