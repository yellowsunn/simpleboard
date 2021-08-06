package com.yellowsunn.simpleforum.api.dto.comment;

import com.yellowsunn.simpleforum.domain.comment.Comment;
import lombok.Data;

@Data
public class CommentGetDto {
    private Long id;
    private Long parentId;
    private String username;
    private String nickname;
    private String content;

    public CommentGetDto(Comment comment) {
        id = comment.getId();
        parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        content = comment.getContent();
        if (comment.getUser() != null) {
            username = comment.getUser().getUsername();
            nickname = comment.getUser().getNickname();
        }
    }
}
