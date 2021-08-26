package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.comment.CommentGetDto;
import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.comment.repository.CommentRepository;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.repository.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public Slice<CommentGetDto> getCursorBasedComments(Long postId, String cursor, Pageable pageable) {
        return commentRepository.findCursorBasedSliceByPostId(postId, cursor, pageable)
                .map(CommentGetDto::new);
    }

    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdQuery(commentId)
                .orElseThrow(NotFoundException::new);

        checkSameUser(userId, comment);

        if (isNotReply(comment)) {
            commentRepository.deleteAllByParentIdQuery(commentId);
        } else {
            commentRepository.delete(comment);
        }
    }

    private boolean isNotReply(Comment comment) {
        return comment.getId().equals(comment.getParent().getId());
    }

    private void checkSameUser(Long userId, Comment comment) {
        if (comment.getUser() == null || !userId.equals(comment.getUser().getId())) {
            throw new ForbiddenException();
        }
    }

    private Comment getParentComment(Long parentCommentId) {
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

            checkParentIsValid(parentComment);

            return parentComment;
        }
        return null;
    }

    private void checkParentIsValid(Comment parentComment) {
        if (!parentComment.getId().equals(parentComment.getParent().getId())) {
            throw new IllegalArgumentException("대댓글에는 답글을 달 수 없습니다.");
        }
    }

}
