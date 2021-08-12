package com.yellowsunn.simpleforum.api.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentUploadDto {

    @NotEmpty
    @Size(max = 1000)
    private String content;

    @NotNull
    private Long postId;

    private Long parentId;
}
