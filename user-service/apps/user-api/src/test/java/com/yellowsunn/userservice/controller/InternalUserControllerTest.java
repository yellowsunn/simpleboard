package com.yellowsunn.userservice.controller;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.dto.InternalUserInfoDto;
import com.yellowsunn.userservice.dto.InternalUserSimpleDto;
import com.yellowsunn.userservice.service.InternalUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternalUserController.class)
class InternalUserControllerTest extends RestDocsApiTest {
    @MockBean
    InternalUserService internalUserService;

    @DisplayName("회원 정보 조회")
    @Test
    void findUser() throws Exception {
        var userId = 1L;
        var uuid = UUID.randomUUID().toString();
        given(internalUserService.findUserInfo(userId)).willReturn(new InternalUserInfoDto(
                1L,
                uuid,
                "test@example.com",
                "닉네임",
                "https://example.com/thumbnail/test.png",
                List.of(Provider.EMAIL, Provider.GOOGLE))
        );

        mockMvc.perform(get("/api/internal/v1/users/{userId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-internal-users-by-userId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("userId").description("유저 아이디"),
                                fieldWithPath("uuid").description("유저 UUID"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("nickName").description("닉네임"),
                                fieldWithPath("thumbnail").description("썸네일 이미지"),
                                fieldWithPath("providers").description("연동 계정")
                        )
                ));
    }

    @DisplayName("회원 정보 조회")
    @Test
    void findUsers() throws Exception {
        var userIds = List.of(1L, 2L);
        given(internalUserService.findUsers(userIds))
                .willReturn(List.of(
                        InternalUserSimpleDto.builder().userId(1L).uuid(UUID.randomUUID().toString()).nickName("닉네임1").thumbnail("https://example.com/thumbnail/test.png").build(),
                        InternalUserSimpleDto.builder().userId(2L).uuid(UUID.randomUUID().toString()).nickName("닉네임2").thumbnail("https://example.com/thumbnail/test.png").build()
                ));

        mockMvc.perform(get("/api/internal/v1/users")
                        .param("id", "1", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-internal-users",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("id").description("유저 아이디 목록")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").description("유저 아이디"),
                                fieldWithPath("[].uuid").description("유저 UUID"),
                                fieldWithPath("[].nickName").description("닉네임"),
                                fieldWithPath("[].thumbnail").description("썸네일 이미지")
                        )
                ));
    }

}
