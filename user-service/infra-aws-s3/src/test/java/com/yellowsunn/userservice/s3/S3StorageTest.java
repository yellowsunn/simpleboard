package com.yellowsunn.userservice.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.yellowsunn.userservice.file.FileUploadRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class S3StorageTest {

    AmazonS3Client amazonS3Client = mock(AmazonS3Client.class);

    S3Storage s3Storage;

    @BeforeEach
    void setUp() {
        s3Storage = new S3Storage(amazonS3Client, "bucket", "https://image-proxy.com");
    }

    @Test
    void uploadFile() {
        // given
        var request = FileUploadRequest.builder()
                .inputStream(InputStream.nullInputStream())
                .originalFileName("test-file.png")
                .contentType("image/png")
                .contentLength(1000)
                .build();

        // when
        String uploadedFile = s3Storage.uploadFile(request, "path");

        // then
        assertThat(uploadedFile).startsWith("https://image-proxy.com/bucket/path").endsWith(".png");
    }

    @Test
    void getDefaultThumbnail() {
        // when
        String thumbnail = s3Storage.getDefaultThumbnail();

        // then
        assertThat(thumbnail).startsWith("https://image-proxy.com/bucket");
    }
}
