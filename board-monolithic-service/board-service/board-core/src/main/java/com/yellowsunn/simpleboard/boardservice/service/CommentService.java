package com.yellowsunn.simpleboard.boardservice.service;

import com.yellowsunn.common.exception.ForbiddenException;
import com.yellowsunn.common.exception.NotFoundException;
import com.yellowsunn.simpleboard.boardservice.domain.comment.Comment;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.boardservice.dto.comment.CommentGetDto;
import com.yellowsunn.simpleboard.boardservice.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleboard.boardservice.repository.CommentRepository;
import com.yellowsunn.simpleboard.boardservice.repository.PostsRepository;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import com.yellowsunn.simpleboard.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<Comment> comments = commentRepository.findByParent(comment);
        comments.add(comment);

        deleteAllComment(comments);
    }

    @Transactional
    public void deleteByPost(Posts post) {
        List<Comment> comments = commentRepository.findByPost(post);
        deleteAllComment(comments);
    }

    private void deleteAllComment(List<Comment> comments) {
        commentRepository.updateAllParentToNullInBatch(comments);
        commentRepository.deleteAllInBatch(comments);
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
