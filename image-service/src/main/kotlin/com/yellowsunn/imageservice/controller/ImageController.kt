package com.yellowsunn.imageservice.controller

import com.yellowsunn.common.annotation.LoginUser
import com.yellowsunn.common.response.ResultResponse
import com.yellowsunn.imageservice.service.ImageService
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration

@RestController
class ImageController(
    private val imageService: ImageService,
) {
    @PostMapping("/api/images/{type}")
    fun uploadImageFile(
        @LoginUser userId: Long,
        @PathVariable type: String,
        @RequestParam image: MultipartFile,
    ): ResultResponse<String> {
        return ResultResponse.ok(
            imageService.uploadImageFile(type, image),
        )
    }

    @GetMapping("/images/{type}/{fileName:.+}")
    fun getImage(@PathVariable type: String, @PathVariable fileName: String): ResponseEntity<ByteArray> {
        val byteArray: ByteArray? = imageService.getImageFile(type, fileName)

        return if (byteArray == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(365)))
                .contentType(findContentType(fileName))
                .body(byteArray)
        }
    }

    private fun findContentType(fileName: String): MediaType {
        val contentType: String? = Files.probeContentType(Paths.get(fileName))
        return if (contentType != null) {
            MediaType.parseMediaType(contentType)
        } else {
            MediaType.IMAGE_PNG
        }
    }
}
