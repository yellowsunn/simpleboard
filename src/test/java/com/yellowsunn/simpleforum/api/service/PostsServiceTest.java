package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @InjectMocks
    PostsService postsService;

    @Mock
    PostsRepository postsRepository;

    @Mock
    User user;

    @Test
    @DisplayName("게시글 저장 성공 - 일반 게시글")
    void save() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.GENERAL);

        //mocking
        given(user.getRole()).willReturn(Role.USER);

        //then
        assertThatNoException()
                .isThrownBy(() -> postsService.save(user, dto));
    }

    @Test
    @DisplayName("게시글 저장 성공 - 공지사항")
    void noticeSave() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.NOTICE);

        //mocking
        given(user.getRole()).willReturn(Role.ADMIN);

        //then
        assertThatNoException()
                .isThrownBy(() -> postsService.save(user, dto));
    }

    @Test
    @DisplayName("게시글 저장 실패 - 관리자만 공지사항을 작성할 수 있다.")
    void failedToNoticeSave() throws Exception {
        //given
        PostsUploadDto dto = getTestPostsUploadDto(PostType.NOTICE);

        //mocking
        given(user.getRole()).willReturn(Role.USER);

        //then
        assertThatThrownBy(() -> postsService.save(user, dto))
                .isInstanceOf(ForbiddenException.class);
    }

    PostsUploadDto getTestPostsUploadDto(PostType type) {
        return PostsUploadDto.builder()
                .title("title")
                .content("content")
                .type(type)
                .build();
    }

}