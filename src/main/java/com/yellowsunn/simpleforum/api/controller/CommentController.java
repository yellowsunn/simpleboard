package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.argumentresolver.LoginId;
import com.yellowsunn.simpleforum.api.dto.comment.CommentGetDto;
import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.api.service.CommentService;
import com.yellowsunn.simpleforum.api.util.RefererFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final RefererFilter refererFilter;

    @PostMapping
    public ResponseEntity<Void> upload(@LoginId Long userId,
                                       @Validated @RequestBody CommentUploadDto commentUploadDto,
                                       BindingResult bindingResult) {
        refererFilter.check("/posts/*");
        checkValidation(bindingResult);
        Long id = commentService.upload(userId, commentUploadDto);
        return ResponseEntity.created(URI.create("/api/comments/" + id))
                .body(null);
    }

    @GetMapping
    public Slice<CommentGetDto> getComments(@RequestParam Long postId, @RequestParam(required = false) String cursor, Pageable pageable) {
        return commentService.getCursorBasedComments(postId, cursor, pageable);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@LoginId Long userId, @PathVariable Long commentId) {
        refererFilter.check("/posts/*");
        commentService.delete(userId, commentId);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    }
}
