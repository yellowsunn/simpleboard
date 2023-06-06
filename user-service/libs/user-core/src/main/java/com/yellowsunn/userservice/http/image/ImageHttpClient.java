package com.yellowsunn.userservice.http.image;

import java.io.InputStream;

public interface ImageHttpClient {
    String uploadThumbnailImage(String originalFilename, InputStream inputStream);
}
