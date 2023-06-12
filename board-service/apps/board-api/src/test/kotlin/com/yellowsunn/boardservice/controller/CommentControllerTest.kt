package com.yellowsunn.boardservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.boardservice.command.dto.CommentSavedDto
import com.yellowsunn.boardservice.command.facade.CommentFacade
import com.yellowsunn.boardservice.dto.CommentSaveRequestDto
import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
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

@WebMvcTest(CommentController::class)
class CommentControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var commentFacade: CommentFacade

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun createComment() {
        val commentId = 1L
        val requestDto = CommentSaveRequestDto(
            content = "댓글 본문",
            imageUrl = "https://emxaple.com/image.jpg",
        )
        every { commentFacade.saveComment(any()) } returns CommentSavedDto(
            commentId = commentId,
            nickName = "닉네임",
            thumbnail = "https://example.com/thumbnail.png",
            content = requestDto.content!!,
            imageUrl = requestDto.imageUrl,
            parentCommentId = null,
            baseCommentId = commentId,
        )

        mockMvc.perform(
            post("/api/v2/articles/{articleId}/comments", 1L)
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)),
        )
            .andExpect(status().isCreated)
            .andDo(print())
            .andDo(
                document(
                    "post-comment",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    requestFields(
                        fieldWithPath("content").description("댓글 본문").optional(),
                        fieldWithPath("imageUrl").description("댓글 첨부 이미지").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.commentId").description("댓글 id"),
                        fieldWithPath("data.nickName").description("댓글 작성자"),
                        fieldWithPath("data.thumbnail").description("댓글 작성자 썸네일").optional(),
                        fieldWithPath("data.content").description("댓글 본문"),
                        fieldWithPath("data.imageUrl").description("댓글 첨부 이미지"),
                        fieldWithPath("data.parentCommentId").description("부모 댓글 id (답글인 경우)").optional(),
                        fieldWithPath("data.baseCommentId").description("기준 댓글 id"),
                    ),
                ),
            )
    }

    @Test
    fun createReplyComment() {
        val parentCommentId = 1L
        val commentId = 2L

        val requestDto = CommentSaveRequestDto(
            content = "댓글 본문",
            imageUrl = "https://emxaple.com/image.jpg",
        )
        every { commentFacade.saveComment(any()) } returns CommentSavedDto(
            commentId = commentId,
            nickName = "닉네임",
            thumbnail = "https://example.com/thumbnail.png",
            content = requestDto.content!!,
            imageUrl = requestDto.imageUrl,
            parentCommentId = parentCommentId,
            baseCommentId = parentCommentId,
        )

        mockMvc.perform(
            post("/api/v2/articles/{articleId}/comments/{commentId}", 1L, 1L)
                .header(USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)),
        )
            .andExpect(status().isCreated)
            .andDo(print())
            .andDo(
                document(
                    "post-reply-comment",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                        parameterWithName("commentId").description("부모 댓글 id (답글을 달고자 하는 댓글 id)"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    requestFields(
                        fieldWithPath("content").description("댓글 본문").optional(),
                        fieldWithPath("imageUrl").description("댓글 첨부 이미지").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.commentId").description("댓글 id"),
                        fieldWithPath("data.nickName").description("댓글 작성자"),
                        fieldWithPath("data.thumbnail").description("댓글 작성자 썸네일").optional(),
                        fieldWithPath("data.content").description("댓글 본문"),
                        fieldWithPath("data.imageUrl").description("댓글 첨부 이미지"),
                        fieldWithPath("data.parentCommentId").description("부모 댓글 id (답글인 경우)").optional(),
                        fieldWithPath("data.baseCommentId").description("기준 댓글 id"),
                    ),
                ),
            )
    }

    @Test
    fun deleteComment() {
        val commentId = 1L
        val articleId = 1L
        val userId = 1L
        every { commentFacade.deleteComment(commentId, articleId, userId) } returns true

        mockMvc.perform(
            delete("/api/v2/articles/{articleId}/comments/{commentId}", articleId, commentId)
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "delete-comment",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                        parameterWithName("commentId").description("댓글 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("댓글 삭제 여부"),
                    ),
                ),
            )
    }

    @Test
    fun likeComment() {
        val commentId = 1L
        val articleId = 1L
        val userId = 1L
        every { commentFacade.likeComment(commentId, articleId, userId) } returns true

        mockMvc.perform(
            put("/api/v2/articles/{articleId}/comments/{commentId}/like", articleId, commentId)
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "put-like-comment",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                        parameterWithName("commentId").description("댓글 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("댓글 좋아요 여부"),
                    ),
                ),
            )
    }

    @Test
    fun undoLikeComment() {
        val commentId = 1L
        val articleId = 1L
        val userId = 1L
        every { commentFacade.undoLikeComment(commentId, articleId, userId) } returns true

        mockMvc.perform(
            delete("/api/v2/articles/{articleId}/comments/{commentId}/like", articleId, commentId)
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "delete-like-comment",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                        parameterWithName("commentId").description("댓글 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("댓글 좋아요 취소 여부"),
                    ),
                ),
            )
    }
}
