package com.yellowsunn.simpleforum.api.dto.posts;

import com.yellowsunn.simpleforum.domain.posts.PostType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostsGetDto {

    private Long id;
    private PostType type;
    private String username;
    private String nickname;
    private String title;
    private String content;
    private Long hit;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
