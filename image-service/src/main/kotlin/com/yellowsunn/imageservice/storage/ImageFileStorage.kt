package com.yellowsunn.imageservice.storage

import com.yellowsunn.common.constant.StorageType
import com.yellowsunn.imageservice.dto.FileUploadRequest

interface ImageFileStorage {
    fun uploadImageFile(request: FileUploadRequest, storageType: StorageType): String
    fun findImage(storageType: StorageType, fileName: String): ByteArray?
}
