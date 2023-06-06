package com.yellowsunn.imageservice.storage

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.dto.FileUploadRequest
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class S3ImageFileStorage(
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    @Value("\${image-base-url}") private val imageBaseUrl: String,
) : ImageFileStorage {
    override fun uploadImageFile(request: FileUploadRequest, storageType: StorageType): String {
        val extension: String = FilenameUtils.getExtension(request.originalFileName)
        val fileName = "${storageType.path}/${UUID.randomUUID()}.$extension"

        val metadata = generateObjectMetadata(request)
        val objectRequest = PutObjectRequest(bucket, fileName, request.inputStream, metadata)
        amazonS3Client.putObject(objectRequest)

        return "$imageBaseUrl/images/$fileName"
    }

    override fun findImage(storageType: StorageType, fileName: String): ByteArray? {
        val fullPath = "${storageType.path}/$fileName"
        val s3Object: S3Object? = amazonS3Client.getObject(bucket, fullPath)
        return s3Object?.objectContent?.readAllBytes()
    }

    private fun generateObjectMetadata(request: FileUploadRequest) = ObjectMetadata()
        .apply {
            contentType = request.contentType
            contentLength = request.contentLength
        }
}
