package com.yellowsunn.imageservice.service

import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.dto.FileUploadRequest
import com.yellowsunn.imageservice.storage.ImageFileStorage
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageFileStorage: ImageFileStorage,
) {
    fun uploadImageFile(type: String, imageFile: MultipartFile): String {
        if (isNotImageType(imageFile.contentType)) {
            throw IllegalArgumentException("이미지 형식이 아닙니다.")
        }

        val fileUploadRequest = generateFileUploadRequest(imageFile)

        return imageFileStorage.uploadImageFile(fileUploadRequest, StorageType.convertFrom(type))
    }

    fun getImageFile(type: String, fileName: String): ByteArray? {
        return try {
            imageFileStorage.findImage(StorageType.convertFrom(type), fileName)
        } catch (e: Exception) {
            null
        }
    }

    private fun isNotImageType(contentType: String?): Boolean {
        return !(contentType?.startsWith("image/") ?: false)
    }

    private fun generateFileUploadRequest(imageFile: MultipartFile): FileUploadRequest = FileUploadRequest(
        inputStream = imageFile.inputStream,
        originalFileName = imageFile.originalFilename!!,
        contentType = imageFile.contentType!!,
        contentLength = imageFile.size,
    )
}
