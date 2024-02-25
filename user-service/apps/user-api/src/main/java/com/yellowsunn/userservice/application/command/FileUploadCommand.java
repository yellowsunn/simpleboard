package com.yellowsunn.userservice.application.command;

import java.io.InputStream;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FileUploadCommand(
        @NonNull String originalFileName,
        @NonNull InputStream inputStream
) {

}
