package com.yellowsunn.imageservice.service

import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.storage.ImageFileStorage
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.InputStream

class ImageServiceTest {

    private lateinit var sut: ImageService

    private val imageFileStorage: ImageFileStorage = mockk()

    @BeforeEach
    fun setUp() {
        sut = ImageService(imageFileStorage)
    }

    @DisplayName("이미지 업로드")
    @Test
    fun uploadImageFile() {
        val type: String = "thumbnail"
        val imageFile = MockMultipartFile("name", "test.png", "image/png", InputStream.nullInputStream())
        every {
            imageFileStorage.uploadImageFile(
                any(),
                StorageType.THUMBNAIL,
            )
        } returns "https://example.com/test.png"

        val imageUrl = sut.uploadImageFile(type, imageFile)

        assertThat(imageUrl).isEqualTo("https://example.com/test.png")
    }

    @DisplayName("이미지 형식이 아닌 경우, 이미지 업로드 실패")
    @Test
    fun uploadImageFile_failed_when_not_image_type() {
        val type: String = "thumbnail"
        val textFile = MockMultipartFile("name", "test.txt", "text/plain", InputStream.nullInputStream())

        val throwable: Throwable = catchThrowable { sut.uploadImageFile(type, textFile) }

        assertThat(throwable).isInstanceOf(IllegalArgumentException::class.java)
    }
}
