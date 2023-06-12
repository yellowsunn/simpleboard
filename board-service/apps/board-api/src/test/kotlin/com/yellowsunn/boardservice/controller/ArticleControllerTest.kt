package com.yellowsunn.boardservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.boardservice.command.facade.ArticleFacade
import com.yellowsunn.boardservice.dto.ArticleSaveRequestDto
import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ArticleController::class)
class ArticleControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var articleFacade: ArticleFacade

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun createArticle() {
        val requestDto = ArticleSaveRequestDto(
            title = "게시글 제목",
            body = "<p>게시글 본문입니다.</p>",
        )
        every { articleFacade.saveArticle(any()) } returns 1L

        mockMvc.perform(
            post("/api/v2/articles")
                .header(USER_ID, 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)),
        )
            .andExpect(status().isCreated)
            .andDo(print())
            .andDo(
                document(
                    "post-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    requestFields(
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("body").description("게시글 본문"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("작성된 게시글 id"),
                    ),
                ),
            )
    }

    @Test
    fun updateArticle() {
        val requestDto = ArticleSaveRequestDto(
            title = "수정된 게시글 제목",
            body = "<p>수정된 게시글 본문입니다.</p>",
        )
        every { articleFacade.updateArticle(any()) } returns true

        mockMvc.perform(
            put("/api/v2/articles/{articleId}", 1L)
                .header(USER_ID, 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "put-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    requestFields(
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("body").description("게시글 본문"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("수정 완료 여부"),
                    ),
                ),
            )
    }

    @Test
    fun deleteArticle() {
        val userId = 1L
        val articleId = 1L
        every { articleFacade.deleteArticle(userId, articleId) } returns true

        mockMvc.perform(
            delete("/api/v2/articles/{articleId}", articleId)
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "delete-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("삭제 완료 여부"),
                    ),
                ),
            )
    }

    @Test
    fun likeArticle() {
        every { articleFacade.likeArticle(any()) } returns true

        mockMvc.perform(
            put("/api/v2/articles/{articleId}/like", 1L)
                .header(USER_ID, 1L),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "put-like-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("좋아요 완료 여부"),
                    ),
                ),
            )
    }

    @Test
    fun undoLikeArticle() {
        every { articleFacade.undoLikeArticle(any()) } returns true

        mockMvc.perform(
            delete("/api/v2/articles/{articleId}/like", 1L)
                .header(USER_ID, 1L),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "delete-like-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("좋아요 완료 여부"),
                    ),
                ),
            )
    }
}
