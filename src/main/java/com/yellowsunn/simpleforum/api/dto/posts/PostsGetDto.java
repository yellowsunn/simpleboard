package com.yellowsunn.simpleforum.api.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostsGetDto {

    private Long id;
    private PostType type;
    private String username;
    private String nickname;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    public PostsGetDto(Posts post) {
        this.id = post.getId();
        this.type = post.getType();
        this.username = post.getUser() != null ? post.getUser().getUsername() : null;
        this.nickname = post.getUser() != null ? post.getUser().getNickname() : null;
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.lastModifiedDate = post.getLastModifiedDate();
    }
}
