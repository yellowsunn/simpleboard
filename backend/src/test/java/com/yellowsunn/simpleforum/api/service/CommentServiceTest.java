package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.comment.repository.CommentRepository;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.repository.UserRepository;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostsRepository postsRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    User mockUser;
    @Mock
    Posts mockPost;
    @Mock
    Comment mockComment;

    Long userId = 1L;
    Long postId = 2L;

    @Test
    @DisplayName("댓글 업로드 성공")
    void upload() {
        //given
        Long saveCommentId = 3L;
        CommentUploadDto dto = getTestCommentUploadDto(null);

        //mocking
        given(userRepository.findById(any())).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(any())).willReturn(Optional.of(mockPost));
        given(commentRepository.save(any())).willReturn(mockComment);
        given(mockComment.getId()).willReturn(saveCommentId);

        //when
        Long id = commentService.upload(userId, dto);

        //then
        assertThat(id).isEqualTo(saveCommentId);
    }

    @Test
    @DisplayName("댓글 업로드 실패 - 회원를 찾을 수 없음")
    void notFoundUserForUpload() {
        //given
        CommentUploadDto dto = getTestCommentUploadDto(null);

        //mocking
        given(userRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> commentService.upload(any(), dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("댓글 업로드 실패 - 게시글을 찾을 수 없음")
    void notFoundPostForUpload() {
        //given
        CommentUploadDto dto = getTestCommentUploadDto(null);

        //mocking
        given(userRepository.findById(any())).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> commentService.upload(any(), dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("댓글 업로드 실패 - 대댓글에는 답글을 달 수 없음")
    void invalidParentUpload() {
        //given
        Long parentCommentId = 3L;
        CommentUploadDto dto = getTestCommentUploadDto(parentCommentId);

        //mocking
        given(userRepository.findById(any())).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(any())).willReturn(Optional.of(mockPost));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(mockComment));
        given(mockComment.getParent()).willReturn(new Comment());

        //then
        assertThatThrownBy(() -> commentService.upload(any(), dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    CommentUploadDto getTestCommentUploadDto(Long parentCommentId) {
        CommentUploadDto dto = new CommentUploadDto();
        dto.setPostId(postId);
        dto.setContent("content");
        dto.setParentId(parentCommentId);

        return dto;
    }
}