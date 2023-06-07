package com.yellowsunn.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.dto.UserInfoUpdateCommand;
import com.yellowsunn.userservice.dto.UserInfoUpdateRequestDto;
import com.yellowsunn.userservice.dto.UserMyInfoDto;
import com.yellowsunn.userservice.facade.UserFacade;
import com.yellowsunn.userservice.file.FileUploadRequest;
import com.yellowsunn.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.yellowsunn.common.constant.CommonHeaderConst.USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExternalUserController.class)
class ExternalUserControllerTest extends RestDocsApiTest {
    @MockBean
    UserFacade userFacade;

    @MockBean
    UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("회원 정보 조회")
    @Test
    void findMyInfo() throws Exception {
        var userId = 1L;
        given(userService.findUserInfo(userId)).willReturn(UserMyInfoDto.builder()
                .email("test@example.com")
                .nickName("닉네임")
                .thumbnail("https://example.com/thumbnail.png")
                .providers(List.of(Provider.EMAIL, Provider.GOOGLE))
                .build());

        mockMvc.perform(get("/api/v2/users/me")
                        .header(USER_ID, userId)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-users-me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-user-id").description("로그인한 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.nickName").description("닉네임"),
                                fieldWithPath("data.thumbnail").description("썸네일"),
                                fieldWithPath("data.providers").description("연동 계정")
                        )
                ));
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMyInfo() throws Exception {
        var userId = 1L;
        var requestDto = new UserInfoUpdateRequestDto("수정된닉네임");
        given(userService.changeUserInfo(any(UserInfoUpdateCommand.class))).willReturn(true);

        mockMvc.perform(put("/api/v2/users/me")
                        .header(USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("put-users-me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-user-id").description("로그인한 유저 id")
                        ),
                        requestFields(
                                fieldWithPath("nickName").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("수정 성공 여부")
                        )
                ));
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteMyInfo() throws Exception {
        mockMvc.perform(delete("/api/v2/users/me")
                        .header(USER_ID, 1L)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-users-me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-user-id").description("로그인한 유저 id")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("탈퇴 성공 여부")
                        )
                ));
    }

    @DisplayName("섬네일 이미지 수정")
    @Test
    void updateMyThumbnail() throws Exception {
        var userId = 1L;
        var imageFile = new MockMultipartFile("thumbnail", "test.png", "image/png", "<< png data >>".getBytes());
        given(userFacade.updateUserThumbnail(eq(userId), any(FileUploadRequest.class)))
                .willReturn("http://localhost:8080/thumbnail/test.png");

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/v2/users/me/thumbnail")
                        .file(imageFile)
                        .header(USER_ID, 1L)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("patch-users-me-thumbnail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-user-id").description("로그인한 유저 id")
                        ),
                        requestParts(
                                partWithName("thumbnail").description("업로드할 이미지")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("변경된 썸네일 이미지 url")
                        )
                ));
    }
}
