package com.yellowsunn.userservice.infrastructure.http.image;

import java.io.InputStream;
import lombok.EqualsAndHashCode;
import org.springframework.core.io.InputStreamResource;

@EqualsAndHashCode(callSuper = true)
public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    public MultipartInputStreamFileResource(String filename, InputStream inputStream) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public long contentLength() {
        return -1;
    }
}
