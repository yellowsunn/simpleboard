package com.yellowsunn.simpleboard.api.dto.file;

import com.yellowsunn.simpleboard.domain.file.File;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileUploadDto {

    private String storeFileName;

    public File convertToFileEntity(Posts post) {
        return File.builder()
                .storeName(storeFileName)
                .post(post)
                .build();
    }
}
