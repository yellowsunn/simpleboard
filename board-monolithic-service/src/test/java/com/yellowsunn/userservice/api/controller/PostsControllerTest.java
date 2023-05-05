package com.yellowsunn.userservice.api.controller;

import com.yellowsunn.simpleboard.api.controller.PostsController;
import com.yellowsunn.userservice.api.SessionConst;
import com.yellowsunn.simpleboard.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleboard.api.service.PostHitService;
import com.yellowsunn.simpleboard.api.service.PostsIntegrationService;
import com.yellowsunn.simpleboard.api.service.PostsService;
import com.yellowsunn.simpleboard.api.util.RefererFilter;
import com.yellowsunn.simpleboard.domain.posts.PostType;
import com.yellowsunn.simpleboard.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleboard.userservice.domain.user.Role;
import com.yellowsunn.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsController.class)
class PostsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PostsIntegrationService postsIntegrationService;

    @MockBean
    PostsService postsService;

    @MockBean
    PostHitService postHitService;

    @MockBean
    PostsRepository postsRepository;

    @MockBean
    RefererFilter refererFilter;

    Long userId = 1L;
    Long postId = 2L;

    @Test
    @DisplayName("업로드 성공")
    void upload() throws Exception {
        //given
        MockHttpServletRequestBuilder request = uploadRequest();
        setLoginSession(request, Role.USER);
        setParameters(request, "title", "content", PostType.GENERAL);

        //then
        mvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("업로드 실패 - 인증 실패")
    void unauthorizedForUpload() throws Exception {
        //given
        MockHttpServletRequestBuilder request = uploadRequest();
        setParameters(request, "title", "content", PostType.GENERAL);

        //then
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("업로드 실패 - 검증 실패")
    void validationFailedForUpload() throws Exception {
        //given
        MockHttpServletRequestBuilder request = uploadRequest();
        setLoginSession(request, Role.USER);
        setParameters(request, " ", " ", null);

        //then
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 조회 성공")
    void getPost() throws Exception {
        //given
        PostsGetDto dto = PostsGetDto.builder().build();
        dto.setLastModifiedDate(LocalDateTime.now());

        MockHttpServletRequestBuilder request = findPost(postId);
        setLoginSession(request, Role.USER);

        //mocking
        given(postsService.findPost(postId)).willReturn(dto);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 조회 실패 - 인증 실패")
    void unauthorizedForGetPost() throws Exception {
        //given
        MockHttpServletRequestBuilder request = findPost(postId);

        //then
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("게시글 조회 실패 - 찾을 수 없음")
    void notFoundGetPost() throws Exception {
        //given
        MockHttpServletRequestBuilder request = findPost(postId);
        setLoginSession(request, Role.USER);

        //mocking
        given(postsService.findPost(postId)).willThrow(NotFoundException.class);

        //then
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("업로드 성공")
    void edit() throws Exception {
        //given
        MockHttpServletRequestBuilder request = editRequest(postId);
        setLoginSession(request, Role.USER);
        setParameters(request, "title", "content", PostType.GENERAL);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("업로드 실패 - 인증 실패")
    void unauthorizedForEdit() throws Exception {
        //given
        MockHttpServletRequestBuilder request = editRequest(postId);
        setParameters(request, "title", "content", PostType.GENERAL);

        //then
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("업로드 실패 - 검증 실패")
    void validationFailedForEdit() throws Exception {
        //given
        MockHttpServletRequestBuilder request = editRequest(postId);
        setLoginSession(request, Role.USER);
        setParameters(request, " ", " ", null);

        //then
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("삭제 성공")
    void successDelete() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteRequest(postId);
        setLoginSession(request, Role.USER);

        //then
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("업로드 실패 - 인증 실패")
    void unauthorizedForDelete() throws Exception {
        //given
        MockHttpServletRequestBuilder request = deleteRequest(postId);

        //then
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }


    private void setParameters(MockHttpServletRequestBuilder request,
                               String title, String content, PostType type) {
        request.param("title", title);
        request.param("content", content);
        request.param("type", type != null ? type.name() : null);
    }

    private void setLoginSession(MockHttpServletRequestBuilder builder, Role userRole) {
        builder.sessionAttr(SessionConst.USER_ID, userId);
        builder.sessionAttr(SessionConst.USER_ROLE, userRole);
    }

    private MockHttpServletRequestBuilder uploadRequest() {
        return post("/api/posts/");
    }

    private MockHttpServletRequestBuilder findPost(Long id) {
        return get("/api/posts/" + id);
    }

    private MockHttpServletRequestBuilder editRequest(Long id) {
        return put("/api/posts/" + id);
    }

    private MockHttpServletRequestBuilder deleteRequest(Long id) {
        return delete("/api/posts/" + id);
    }
}
