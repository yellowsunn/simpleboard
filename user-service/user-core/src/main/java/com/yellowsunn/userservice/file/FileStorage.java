package com.yellowsunn.userservice.file;

public interface FileStorage {
    String uploadFile(FileUploadRequest request, String path);

    String getDefaultThumbnail();
}
