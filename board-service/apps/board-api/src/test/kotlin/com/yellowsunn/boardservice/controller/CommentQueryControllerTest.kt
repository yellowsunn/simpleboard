package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.query.dto.CommentDocumentPageDto
import com.yellowsunn.boardservice.query.dto.UserCommentDocumentPageDto
import com.yellowsunn.boardservice.query.service.CommentQueryService
import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZonedDateTime
import java.util.UUID

@WebMvcTest(CommentQueryController::class)
class CommentQueryControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var commentQueryService: CommentQueryService

    @Test
    fun getComments() {
        val articleId = 1L
        val page = 1
        val size = 10
        every { commentQueryService.findComments(articleId, page, size) } returns CommentDocumentPageDto(
            comments = listOf(
                CommentDocumentPageDto.CommentDocumentDto(
                    commentId = 1L,
                    articleId = articleId,
                    parentCommentId = null,
                    baseCommentId = 1L,
                    content = "댓글 본문",
                    imageUrl = "https://example.com/image.png",
                    likeCount = 10L,
                    userUUID = UUID.randomUUID().toString(),
                    nickName = "닉네임",
                    userThumbnail = "https://example.com/thumbnail.png",
                    savedAt = ZonedDateTime.now(),
                ),
            ),
            page = page,
            size = size,
            totalPages = 1,
            totalElements = 1L,
            numberOfElements = 1,
        )

        mockMvc.perform(
            get("/api/v2/articles/{articleId}/comments", articleId)
                .queryParam("page", page.toString())
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-comments",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    queryParameters(
                        parameterWithName("page").description("페이지").optional(),
                        parameterWithName("size").description("페이지 사이즈").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.page").description("페이지"),
                        fieldWithPath("data.size").description("페이지 사이즈"),
                        fieldWithPath("data.totalPages").description("총 페이지 수"),
                        fieldWithPath("data.numberOfElements").description("반환된 데이터 개수"),
                        fieldWithPath("data.totalElements").description("전체 데이터 개수"),
                        fieldWithPath("data.comments").description("댓글 목록"),
                        fieldWithPath("data.comments[].commentId").description("댓글 제목"),
                        fieldWithPath("data.comments[].articleId").description("게시글 id"),
                        fieldWithPath("data.comments[].parentCommentId").description("부모 댓글 id (답글인 경우)").optional(),
                        fieldWithPath("data.comments[].baseCommentId").description("기준 댓글 id"),
                        fieldWithPath("data.comments[].content").description("댓글 본문"),
                        fieldWithPath("data.comments[].imageUrl").description("댓글 첨부 이미지").optional(),
                        fieldWithPath("data.comments[].likeCount").description("댓글 좋아요 수"),
                        fieldWithPath("data.comments[].userUUID").description("댓글 작성자 UUID"),
                        fieldWithPath("data.comments[].nickName").description("댓글 작성자 닉네임"),
                        fieldWithPath("data.comments[].userThumbnail").description("댓글 작성자 썸네일").optional(),
                        fieldWithPath("data.comments[].savedAt").description("댓글 작성된 시각"),
                        fieldWithPath("data.comments[].nickName").description("게시글 작성자"),
                    ),
                ),
            )
    }

    @Test
    fun getMyComments() {
        val userId = 1L
        val page = 1
        val size = 10
        every { commentQueryService.findUserComments(userId, page, size) } returns UserCommentDocumentPageDto(
            comments = listOf(
                UserCommentDocumentPageDto.UserCommentDocumentDto(
                    commentId = 1L,
                    articleId = 1L,
                    content = "댓글 본문",
                    imageUrl = "https://example.com/image.png",
                    savedAt = ZonedDateTime.now(),
                ),
            ),
            page = page,
            size = size,
            totalPages = 1,
            totalElements = 1L,
            numberOfElements = 1,
        )

        mockMvc.perform(
            get("/api/v2/comments/me")
                .header(USER_ID, 1L)
                .queryParam("page", page.toString())
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-my-comments",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    queryParameters(
                        parameterWithName("page").description("페이지").optional(),
                        parameterWithName("size").description("페이지 사이즈").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.page").description("페이지"),
                        fieldWithPath("data.size").description("페이지 사이즈"),
                        fieldWithPath("data.totalPages").description("총 페이지 수"),
                        fieldWithPath("data.numberOfElements").description("반환된 데이터 개수"),
                        fieldWithPath("data.totalElements").description("전체 데이터 개수"),
                        fieldWithPath("data.comments").description("댓글 목록"),
                        fieldWithPath("data.comments[].commentId").description("댓글 제목"),
                        fieldWithPath("data.comments[].articleId").description("게시글 id"),
                        fieldWithPath("data.comments[].content").description("댓글 본문"),
                        fieldWithPath("data.comments[].imageUrl").description("댓글 첨부 이미지").optional(),
                        fieldWithPath("data.comments[].savedAt").description("댓글 작성된 시각"),
                    ),
                ),
            )
    }

    @Test
    fun getCommentPage() {
        val commentId = 1L
        val size = 10
        every { commentQueryService.findCommentPage(commentId, size) } returns 3

        mockMvc.perform(
            get("/api/v2/comments/{commentId}/page", commentId)
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-comment-page",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("commentId").description("댓글 id"),
                    ),
                    queryParameters(
                        parameterWithName("size").description("페이지 사이즈").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("페이지 오프셋"),
                    ),
                ),
            )
    }
}
