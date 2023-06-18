package com.yellowsunn.boardservice.controller

import com.yellowsunn.boardservice.dto.ArticleDocumentDto
import com.yellowsunn.boardservice.dto.ArticleDocumentPageDto
import com.yellowsunn.boardservice.dto.ArticleReactionDocumentDto
import com.yellowsunn.boardservice.dto.UserArticleDocumentPageDto
import com.yellowsunn.boardservice.service.ArticleQueryService
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

@WebMvcTest(ArticleQueryController::class)
class ArticleQueryControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var articleQueryService: ArticleQueryService

    @Test
    fun getArticles() {
        val page = 1
        val size = 10
        every { articleQueryService.findArticles(page, size) } returns ArticleDocumentPageDto(
            articles = listOf(
                ArticleDocumentPageDto.ArticleDocumentDto(
                    articleId = 1L,
                    thumbnail = "https://example.com/thumbnail.png",
                    title = "게시글 제목",
                    viewCount = 30L,
                    likeCount = 5L,
                    commentCount = 10L,
                    savedAt = ZonedDateTime.now(),
                    nickName = "닉네임",
                ),
            ),
            page = page,
            size = size,
            totalElements = 1L,
            totalPages = 1,
            numberOfElements = 1,
        )

        mockMvc.perform(
            get("/api/v2/articles")
                .queryParam("page", page.toString())
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-articles",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
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
                        fieldWithPath("data.articles").description("게시글 목록"),
                        fieldWithPath("data.articles[].articleId").description("게시글 id"),
                        fieldWithPath("data.articles[].thumbnail").description("게시글 썸네일"),
                        fieldWithPath("data.articles[].title").description("게시글 제목"),
                        fieldWithPath("data.articles[].viewCount").description("게시글 조회수"),
                        fieldWithPath("data.articles[].likeCount").description("게시글 좋아요 개수"),
                        fieldWithPath("data.articles[].commentCount").description("게시글에 달린 댓글 개수"),
                        fieldWithPath("data.articles[].savedAt").description("게시글이 작성된 시각"),
                        fieldWithPath("data.articles[].nickName").description("게시글 작성자"),
                    ),
                ),
            )
    }

    @Test
    fun getMyArticles() {
        val userId = 1L
        val page = 1
        val size = 10
        every { articleQueryService.findUserArticles(userId, page, size) } returns UserArticleDocumentPageDto(
            articles = listOf(
                UserArticleDocumentPageDto.UserArticleDocumentDto(
                    articleId = 1L,
                    title = "게시글 제목",
                    savedAt = ZonedDateTime.now(),
                ),
            ),
            page = page,
            size = size,
            totalElements = 1L,
            totalPages = 1,
            numberOfElements = 1,
        )

        mockMvc.perform(
            get("/api/v2/articles/me")
                .header(USER_ID, 1L)
                .queryParam("page", page.toString())
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-my-articles",
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
                        fieldWithPath("data.articles").description("게시글 목록"),
                        fieldWithPath("data.articles[].articleId").description("게시글 id"),
                        fieldWithPath("data.articles[].title").description("게시글 제목"),
                        fieldWithPath("data.articles[].savedAt").description("게시글이 작성된 시각"),
                    ),
                ),
            )
    }

    @Test
    fun getArticle() {
        val articleId = 1L
        every { articleQueryService.findArticleById(articleId) } returns ArticleDocumentDto(
            articleId = articleId,
            title = "게시글 제목",
            body = "<p>게시글 본문</p>",
            viewCount = 30L,
            likeCount = 5L,
            commentCount = 10L,
            savedAt = ZonedDateTime.now(),
            user = ArticleDocumentDto.ArticleDocumentUser(
                uuid = UUID.randomUUID().toString(),
                thumbnail = "https://example.com/thumbnail.png",
                nickName = "닉네임",
            ),
        )

        mockMvc.perform(
            get("/api/v2/articles/{articleId}", articleId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-article",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("articleId").description("게시글 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.articleId").description("게시글 id"),
                        fieldWithPath("data.title").description("게시글 제목"),
                        fieldWithPath("data.body").description("게시글 본문"),
                        fieldWithPath("data.viewCount").description("게시글 조회수"),
                        fieldWithPath("data.likeCount").description("게시글 좋아요 개수"),
                        fieldWithPath("data.commentCount").description("게시글에 달린 댓글 개수"),
                        fieldWithPath("data.savedAt").description("게시글이 작성된 시각"),
                        fieldWithPath("data.user").description("게시글 작성자"),
                        fieldWithPath("data.user.uuid").description("게시글 작성자 UUID"),
                        fieldWithPath("data.user.thumbnail").description("게시글 작성자 썸네일"),
                        fieldWithPath("data.user.nickName").description("게시글 작성자 닉네임"),
                    ),
                ),
            )
    }

    @Test
    fun getArticleReaction() {
        val articleId = 1L
        val userId = 1L
        every { articleQueryService.findReactionByArticleId(articleId, userId) } returns ArticleReactionDocumentDto(
            articleId = articleId,
            isArticleLiked = true,
            likedCommentIds = listOf(1L, 3L, 5L),
        )

        mockMvc.perform(
            get("/api/v2/articles/{articleId}/reaction", articleId)
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-my-article-reaction",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id").optional(),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data.articleId").description("게시글 id").optional(),
                        fieldWithPath("data.isArticleLiked").description("게시글 좋아요 여부").optional(),
                        fieldWithPath("data.likedCommentIds").description("좋아요한 댓글 목록").optional(),
                    ),
                ),
            )
    }
}
