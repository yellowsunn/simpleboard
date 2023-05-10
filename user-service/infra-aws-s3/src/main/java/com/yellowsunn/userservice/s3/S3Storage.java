package com.yellowsunn.userservice.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yellowsunn.userservice.file.FileStorage;
import com.yellowsunn.userservice.file.FileUploadRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class S3Storage implements FileStorage {
    private final AmazonS3Client amazonS3Client;
    private final String bucket;
    private final String imageProxyUrl;

    private static final String DEFAULT_THUMBNAIL = "thumbnail/default-thumbnail.svg";

    public S3Storage(AmazonS3Client amazonS3Client,
                     @Value("${cloud.aws.s3.bucket}") String bucket,
                     @Value("${micro-services.image-proxy-service-url}") String imageProxyUrl) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
        this.imageProxyUrl = imageProxyUrl;
    }

    @Override
    public String uploadFile(FileUploadRequest request, String path) {
        String extension = FilenameUtils.getExtension(request.originalFileName());
        String fileName = String.format("%s/%s.%s", path, UUID.randomUUID(), extension);

        var metadata = generateObjectMetadata(request);
        var objectRequest = new PutObjectRequest(bucket, fileName, request.inputStream(), metadata);
        amazonS3Client.putObject(objectRequest);

        return String.format("%s/%s/%s", imageProxyUrl, bucket, fileName);
    }

    @Override
    public String getDefaultThumbnail() {
        return String.format("%s/%s/%s", imageProxyUrl, bucket, DEFAULT_THUMBNAIL);
    }

    private ObjectMetadata generateObjectMetadata(FileUploadRequest request) {
        var metadata = new ObjectMetadata();
        metadata.setContentType(request.contentType());
        metadata.setContentLength(request.contentLength());
        return metadata;
    }
}
