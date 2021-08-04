package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.comment.CommentRepository;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long upload(Long userId, CommentUploadDto commentUploadDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
        Posts post = postsRepository.findById(commentUploadDto.getPostId())
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        Comment parent = getParentComment(commentUploadDto.getParentCommentId());

        Comment comment = Comment.builder()
                .user(user)
                .content(commentUploadDto.getContent())
                .post(post)
                .parent(parent)
                .build();

        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    private Comment getParentComment(Long parentCommentId) {
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new NotFoundException("부모 댓글을 찾을 수 없습니다."));

            checkParentIsValid(parentComment);

            return parentComment;
        }
        return null;
    }

    private void checkParentIsValid(Comment parentComment) {
        if (parentComment.getParent() != null) {
            throw new IllegalArgumentException("대댓글에는 답글을 달 수 없습니다.");
        }
    }

}
