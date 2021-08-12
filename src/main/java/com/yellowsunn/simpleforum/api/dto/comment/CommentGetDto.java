package com.yellowsunn.simpleforum.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentGetDto {

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDate;

    private Long id;
    private Long parentId;
    private String username;
    private String content;

    public CommentGetDto(Comment comment) {
        createdDate = comment.getCreatedDate();
        id = comment.getId();
        parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        content = comment.getContent();
        if (comment.getUser() != null) {
            username = comment.getUser().getUsername();
        }
    }
}
