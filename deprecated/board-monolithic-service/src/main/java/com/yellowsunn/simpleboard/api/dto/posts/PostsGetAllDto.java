package com.yellowsunn.simpleboard.api.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.yellowsunn.simpleboard.domain.posts.PostType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostsGetAllDto {
    private Long id;
    private PostType type;
    private String title;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdDate;

    private String username;
    private Long hit;
    private long commentSize;
    private boolean hasImage;

    @QueryProjection
    public PostsGetAllDto(Long id, PostType type, String title, LocalDateTime createdDate, String username, Long hit, long commentSize, long imageFileSize) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.createdDate = createdDate;
        this.username = username;
        this.hit = hit;
        this.commentSize = commentSize;
        this.hasImage = imageFileSize > 0;
    }
}
