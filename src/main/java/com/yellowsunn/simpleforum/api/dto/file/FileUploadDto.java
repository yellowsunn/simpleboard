package com.yellowsunn.simpleforum.api.dto.file;

import com.yellowsunn.simpleforum.domain.file.File;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileUploadDto {

    private String uploadFileName;
    private String storeFileName;

    public File convertToFileEntity(Posts post) {
        return File.builder()
                .uploadName(uploadFileName)
                .storeName(storeFileName)
                .post(post)
                .build();
    }
}
