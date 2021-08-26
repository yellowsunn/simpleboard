package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.comment.repository.CommentRepository;
import com.yellowsunn.simpleforum.domain.file.FileRepository;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.repository.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @InjectMocks
    PostsService postsService;

    @Mock
    PostsRepository postsRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    FileRepository fileRepository;

    @Mock
    User mockUser;

    @Mock
    User mockUser2;

    @Mock
    Posts mockPost;

    Long userId = 1L;
    Long postId = 2L;

    @Test
    @DisplayName("게시글 저장 성공 - 일반 게시글")
    void save() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.GENERAL);

        //mocking
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatNoException()
                .isThrownBy(() -> postsService.save(mockUser, dto));
    }

    @Test
    @DisplayName("게시글 저장 성공 - 공지사항")
    void noticeSave() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.NOTICE);

        //mocking
        given(mockUser.getRole()).willReturn(Role.ADMIN);

        //then
        assertThatNoException()
                .isThrownBy(() -> postsService.save(mockUser, dto));
    }

    @Test
    @DisplayName("게시글 저장 실패 - 관리자만 공지사항을 작성할 수 있다.")
    void failedToNoticeSave() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.NOTICE);

        //mocking
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatThrownBy(() -> postsService.save(mockUser, dto))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("게시글 조회 성공")
    void findPost() {
        //given
        Posts post = getTestPosts(PostType.GENERAL);

        //mocking
        given(postsRepository.findPost(postId)).willReturn(Optional.ofNullable(post));

        //then
        assertThat(postsService.findPost(postId)).isNotNull();
    }

    @Test
    @DisplayName("게시글 조회 실패 - 찾을 수 없음")
    void notFoundFindPost() {
        //mocking
        given(postsRepository.findPost(postId)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> postsService.findPost(postId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void edit() {
        //given
        PostsEditDto dto = getTestPostsEditDto(PostType.GENERAL);

        //mocking
        given(postsRepository.findById(postId)).willReturn(Optional.ofNullable(mockPost));
        given(mockPost.getUser()).willReturn(mockUser);
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatNoException().isThrownBy(() -> postsService.edit(postId, userId, dto));
    }

    @Test
    @DisplayName("게시글 수정 성공 - 공지사항")
    void noticeEdit() {
        //given
        PostsEditDto dto = getTestPostsEditDto(PostType.NOTICE);

        //mocking
        given(postsRepository.findById(postId)).willReturn(Optional.ofNullable(mockPost));
        given(mockPost.getUser()).willReturn(mockUser);
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(Role.ADMIN);

        //then
        assertThatNoException().isThrownBy(() -> postsService.edit(postId, userId, dto));
    }

    @Test
    @DisplayName("게시글 수정 실패 - 관리자만 공지사항으로 변경할 수 있다.")
    void failedToNoticeEdit() {
        //given
        PostsEditDto dto = getTestPostsEditDto(PostType.NOTICE);

        //mocking
        given(postsRepository.findById(postId)).willReturn(Optional.ofNullable(mockPost));
        given(mockPost.getUser()).willReturn(mockUser);
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatThrownBy(() -> postsService.edit(postId, userId, dto))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("게시글 수정 실패 - 작성자가 일치하지 않음")
    void forbiddenEdit() {
        //given
        PostsEditDto dto = getTestPostsEditDto(PostType.GENERAL);
        Long otherUserId = 333L;

        //mocking
        given(postsRepository.findById(postId)).willReturn(Optional.ofNullable(mockPost));
        given(mockPost.getUser()).willReturn(mockUser);
        given(mockUser.getId()).willReturn(userId);

        //then
        assertThatThrownBy(() -> postsService.edit(postId, otherUserId, dto))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void delete() {
        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(postId)).willReturn(Optional.of(mockPost));
        given(mockPost.getUser()).willReturn(mockUser);
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatNoException().isThrownBy(() -> postsService.delete(postId, userId));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 사용자를 찾을 수 없음")
    void notFoundUserDelete() {
        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> postsService.delete(postId, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글을 찾을 수 없음")
    void notFoundPostDelete() {
        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(postId)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> postsService.delete(postId, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 아니면 삭제 불가 (USER 권한인 경우)")
    void forbiddenDelete() {
        //given
        Long otherUserId = 333L;

        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(postId)).willReturn(Optional.of(mockPost));
        given(mockPost.getUser()).willReturn(mockUser2);
        given(mockUser2.getId()).willReturn(otherUserId);
        given(mockUser.getId()).willReturn(userId);
        given(mockUser.getRole()).willReturn(Role.USER);

        //then
        assertThatThrownBy(() -> postsService.delete(postId, userId))
                .isInstanceOf(ForbiddenException.class);

    }

    @Test
    @DisplayName("게시글 삭제 성공 - 작성자가 아니어도 삭제 가능 (ADMIN 권한인 경우)")
    void adminDelete() {
        //mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(postsRepository.findById(postId)).willReturn(Optional.of(mockPost));
        given(mockUser.getRole()).willReturn(Role.ADMIN);

        //then
        assertThatNoException().isThrownBy(() -> postsService.delete(postId, userId));
    }

    PostsUploadDto getTestPostsUploadDto(PostType type) {
        return PostsUploadDto.builder()
                .title("title")
                .content("content")
                .type(type)
                .build();
    }

    Posts getTestPosts(PostType type) {
        return Posts.builder()
                .title("title")
                .content("content")
                .type(type)
                .build();
    }

    PostsEditDto getTestPostsEditDto(PostType type) {
        PostsEditDto postsEditDto = new PostsEditDto();
        postsEditDto.setTitle("title");
        postsEditDto.setContent("content");
        postsEditDto.setType(type);

        return postsEditDto;
    }
}