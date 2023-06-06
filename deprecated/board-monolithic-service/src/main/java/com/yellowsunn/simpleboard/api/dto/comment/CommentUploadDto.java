package com.yellowsunn.simpleboard.api.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUploadDto {

    @NotEmpty
    @Size(max = 1000)
    private String content;

    @NotNull
    private Long postId;

    private Long parentId;
}
