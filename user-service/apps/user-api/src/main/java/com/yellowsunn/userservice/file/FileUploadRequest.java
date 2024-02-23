package com.yellowsunn.userservice.file;

import lombok.Builder;
import lombok.NonNull;

import java.io.InputStream;

@Builder
public record FileUploadRequest(
        @NonNull String originalFileName,
        @NonNull InputStream inputStream
) {
}
