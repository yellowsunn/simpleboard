package com.yellowsunn.simpleboard.boardservice.dto.file;

import com.yellowsunn.simpleboard.boardservice.domain.file.File;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
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
