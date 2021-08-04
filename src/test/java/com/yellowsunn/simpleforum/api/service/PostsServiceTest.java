package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
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
    User mockUser;

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
        given(postsRepository.findPostAndUpdateHit(postId)).willReturn(Optional.ofNullable(post));

        //then
        assertThat(postsService.findPost(postId)).isNotNull();
    }

    @Test
    @DisplayName("게시글 조회 실패 - 찾을 수 없음")
    void notFoundFindPost() {
        //mocking
        given(postsRepository.findPostAndUpdateHit(postId)).willReturn(Optional.empty());

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