package com.yellowsunn.notificationservice.controller

import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import com.yellowsunn.common.notification.ArticleLikeNotificationData
import com.yellowsunn.common.notification.CommentNotificationData
import com.yellowsunn.notificationservice.dto.NotificationDocumentPageDto
import com.yellowsunn.notificationservice.service.NotificationService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZonedDateTime

@WebMvcTest(NotificationController::class)
class NotificationControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var notificationService: NotificationService

    @Test
    fun getMyNotifications() {
        val userId = 1L
        val page = 1
        val size = 10
        every { notificationService.findUserNotifications(userId, page, size) } returns NotificationDocumentPageDto(
            notifications = listOf(
                NotificationDocumentPageDto.NotificationDocumentDto(
                    userId = userId,
                    title = "알림 제목",
                    content = "알림 본문",
                    readAt = ZonedDateTime.now(),
                    createdAt = ZonedDateTime.now(),
                    data = CommentNotificationData(1L, 1L),
                ),
                NotificationDocumentPageDto.NotificationDocumentDto(
                    userId = userId,
                    title = "알림 제목",
                    content = "알림 본문",
                    readAt = null,
                    createdAt = ZonedDateTime.now(),
                    data = ArticleLikeNotificationData(1L),
                ),
            ),
            page = page,
            size = size,
            totalPages = 1,
            numberOfElements = 1,
            totalElements = 1L,
        )

        mockMvc.perform(
            get("/api/v1/notifications/me")
                .header(USER_ID, userId)
                .queryParam("page", page.toString())
                .queryParam("size", size.toString()),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-my-notifications",
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
                        fieldWithPath("data.notifications").description("알림 목록"),
                        fieldWithPath("data.notifications[].userId").description("사용자 id"),
                        fieldWithPath("data.notifications[].title").description("알림 제목"),
                        fieldWithPath("data.notifications[].content").description("알림 본문"),
                        fieldWithPath("data.notifications[].readAt").description("읽은 시간").optional(),
                        fieldWithPath("data.notifications[].createdAt").description("알림 생성 시간"),
                        fieldWithPath("data.notifications[].data").description("알림 동적 데이터"),
                        fieldWithPath("data.notifications[].data.type").description("알림 데이터 타입"),
                        fieldWithPath("data.notifications[].data.*").optional().ignored(),
                    ),
                ),
            )
    }

    @Test
    fun readMyNotifications() {
        val userId = 1L
        every { notificationService.readUserNotifications(userId) } returns 3

        mockMvc.perform(
            put("/api/v1/notifications/me/read")
                .header(USER_ID, userId),
        ).andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "read-my-notifications",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("확인한 알림 갯수"),
                    ),
                ),
            )
    }

    @Test
    fun existMyUnreadNotifications() {
        val userId = 1L
        every { notificationService.existUserUnreadNotifications(userId) } returns true

        mockMvc.perform(
            get("/api/v1/notifications/me/unread")
                .header(USER_ID, userId),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "unread-my-notifications",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("확인하지 않은 알림 존재 유무"),
                    ),
                ),
            )
    }
}
