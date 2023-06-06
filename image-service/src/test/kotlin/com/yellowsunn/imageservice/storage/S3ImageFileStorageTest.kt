package com.yellowsunn.imageservice.storage

import com.amazonaws.services.s3.AmazonS3Client
import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.dto.FileUploadRequest
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.InputStream

class S3ImageFileStorageTest {
    private lateinit var sut: S3ImageFileStorage

    private val amazonS3Client = mockk<AmazonS3Client>(relaxed = true)

    @BeforeEach
    fun setUp() {
        sut = S3ImageFileStorage(
            amazonS3Client = amazonS3Client,
            bucket = "test-bucket",
            imageBaseUrl = "https://example.com",
        )
    }

    @Test
    fun uploadImageFile() {
        val request = FileUploadRequest(
            inputStream = InputStream.nullInputStream(),
            originalFileName = "test.png",
            contentType = "image/png",
            contentLength = 1000L,
        )

        val uploadImageFile = sut.uploadImageFile(request, StorageType.THUMBNAIL)

        assertThat(uploadImageFile).startsWith("https://example.com/images/thumbnail/").endsWith(".png")
    }
}
