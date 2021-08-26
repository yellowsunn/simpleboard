package com.yellowsunn.simpleforum.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.simpleforum.api.SessionConst;
import com.yellowsunn.simpleforum.api.dto.comment.CommentUploadDto;
import com.yellowsunn.simpleforum.api.service.CommentService;
import com.yellowsunn.simpleforum.api.util.RefererFilter;
import com.yellowsunn.simpleforum.domain.user.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CommentService commentService;

    @MockBean
    RefererFilter refererFilter;

    ObjectMapper objectMapper = new ObjectMapper();

    private Long userId = 1L;
    private Long postId = 2L;

    @Test
    @DisplayName("업로드 성공")
    void upload() throws Exception {
        //given
        MockHttpServletRequestBuilder request = uploadRequest();
        setLoginSession(request, Role.USER);
        setJsonContent(request, getTestCommentUploadDto(null));

        //then
        mvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("업로드 실패 - 인증 실패")
    void unauthorizedUpload() throws Exception {
        //given
        MockHttpServletRequestBuilder request = uploadRequest();

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

        //then
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    private void setLoginSession(MockHttpServletRequestBuilder builder, Role userRole) {
        builder.sessionAttr(SessionConst.USER_ID, userId);
        builder.sessionAttr(SessionConst.USER_ROLE, userRole);
    }

    private void setJsonContent(MockHttpServletRequestBuilder builder, Object object) throws JsonProcessingException {
        builder
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder uploadRequest() {
        return post("/api/comments/");
    }

    CommentUploadDto getTestCommentUploadDto(Long parentCommentId) {
        CommentUploadDto dto = new CommentUploadDto();
        dto.setPostId(postId);
        dto.setContent("content");
        dto.setParentId(parentCommentId);

        return dto;
    }
}