package com.yellowsunn.imageservice.controller

import com.yellowsunn.common.constant.CommonHeaderConst.USER_ID
import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.service.ImageService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ImageController::class)
class ImageControllerTest : RestDocsApiTest() {
    @Autowired
    lateinit var imageService: ImageService

    @Test
    fun uploadImageFile() {
        val type = "thumbnail"
        val imageFile = MockMultipartFile("image", "test.png", "image/png", "<<png data>>".toByteArray())
        every { imageService.uploadImageFile(type, imageFile) } returns "https://localhost:8080/images/thumbnail/test.png"

        mockMvc.perform(
            multipart("/api/images/{type}", type)
                .file(imageFile)
                .header(USER_ID, 1L),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "post-upload-image",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("업로드 할 이미지 유형. ex) ${formattedStorageTypeValues()}"),
                    ),
                    requestHeaders(
                        headerWithName("x-user-id").description("로그인한 유저 id"),
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

    @Test
    fun getImage() {
        val type = "thumbnail"
        val fileName = "test.png"

        every { imageService.getImageFile(type, fileName) } returns "<<png data>>".toByteArray()

        mockMvc.perform(
            get("/images/{type}/{fileName}", type, fileName),
        )
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-image",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("조회할 할 이미지 유형. ex) ${formattedStorageTypeValues()}"),
                        parameterWithName("fileName").description("이미지 이름. ex) ${formattedStorageTypeValues()}"),
                    ),
                ),
            )
    }

    @Test
    fun getImage_return_404_when_not_found_image() {
        val type = "thumbnail"
        val fileName = "notfound-image.png"

        every { imageService.getImageFile(type, fileName) } returns null

        mockMvc.perform(
            get("/images/{type}/{fileName}", type, fileName),
        )
            .andExpect(status().isNotFound)
            .andDo(print())
            .andDo(
                document(
                    "get-image-not-found",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("조회할 할 이미지 유형. ex) ${formattedStorageTypeValues()}"),
                        parameterWithName("fileName").description("이미지 이름. ex) ${formattedStorageTypeValues()}"),
                    ),
                ),
            )
    }

    private fun formattedStorageTypeValues(): String {
        return StorageType.values().joinToString(", ")
    }
}
