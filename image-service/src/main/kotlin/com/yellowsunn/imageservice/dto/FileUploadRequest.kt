package com.yellowsunn.imageservice.dto

import java.io.InputStream

data class FileUploadRequest(
    val inputStream: InputStream,
    val originalFileName: String,
    val contentType: String,
    val contentLength: Long,
)
