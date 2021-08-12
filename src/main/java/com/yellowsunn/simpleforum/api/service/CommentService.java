package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.comment.CommentGetDto;
import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.comment.CommentRepository;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        Comment parent = getParentComment(commentUploadDto.getParentId());

        Comment comment = Comment.builder()
                .user(user)
                .content(commentUploadDto.getContent())
                .post(post)
                .parent(parent)
                .build();

        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentGetDto> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable)
                .map(CommentGetDto::new);
    }

    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdQuery(commentId)
                .orElseThrow(NotFoundException::new);

        checkSameUser(userId, comment);

        commentRepository.deleteAllByParentIdQuery(commentId);
        commentRepository.delete(comment);
    }

    private void checkSameUser(Long userId, Comment comment) {
        if (comment.getUser() == null || !userId.equals(comment.getUser().getId())) {
            throw new ForbiddenException();
        }
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
