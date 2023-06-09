package com.yellowsunn.notificationservice.controller

import com.yellowsunn.common.annotation.LoginUser
import com.yellowsunn.common.response.ResultResponse
import com.yellowsunn.notificationservice.dto.NotificationDocumentPageDto
import com.yellowsunn.notificationservice.service.NotificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(
    private val notificationService: NotificationService,
) {
    @GetMapping("/api/v1/notifications/me")
    fun getMyNotifications(
        @LoginUser userId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResultResponse<NotificationDocumentPageDto> {
        return ResultResponse.ok(
            notificationService.findUserNotifications(userId, page, size),
        )
    }

    @PutMapping("/api/v1/notifications/me/read")
    fun readMyNotifications(@LoginUser userId: Long): ResultResponse<Long> {
        return ResultResponse.ok(
            notificationService.readUserNotifications(userId),
        )
    }

    @GetMapping("/api/v1/notifications/me/unread")
    fun existMyUnreadNotifications(@LoginUser userId: Long): ResultResponse<Boolean> {
        return ResultResponse.ok(
            notificationService.existUserUnreadNotifications(userId),
        )
    }
}
