package com.yellowsunn.imageservice.controller

import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.service.ImageService
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(InternalImageController::class)
class InternalImageControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var imageService: ImageService

    @Test
    fun uploadImageFile() {
        val type = "thumbnail"
        val imageFile = MockMultipartFile("image", "test.png", "image/png", "<<png data>>".toByteArray())
        every { imageService.uploadImageFile(type, imageFile) } returns "https://localhost:8080/images/thumbnail/test.png"

        mockMvc.perform(
            multipart("/api/internal/images/{type}", type)
                .file(imageFile),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "post-internal-upload-image",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    pathParameters(
                        parameterWithName("type")
                            .description("업로드 할 이미지 유형. ex) ${formattedStorageTypeValues()}"),
                    ),
                    requestParts(
                        partWithName("image").description("업로드할 이미지"),
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("업로드 된 이미지 URL"),
                    ),
                ),
            )
    }

    private fun formattedStorageTypeValues(): String {
        return StorageType.values().joinToString(", ")
    }
}
