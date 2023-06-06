package com.yellowsunn.imageservice.controller

import com.yellowsunn.common.response.ResultResponse
import com.yellowsunn.imageservice.service.ImageService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class InternalImageController(
    private val imageService: ImageService,
) {
    @PostMapping("/api/internal/images/{type}")
    fun uploadImageFile(
        @PathVariable type: String,
        @RequestParam image: MultipartFile,
    ): ResultResponse<String> {
        return ResultResponse.ok(imageService.uploadImageFile(type, image))
    }
}
