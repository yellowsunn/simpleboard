package com.yellowsunn.simpleforum.api.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Long hit;

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
        this.hit = post.getHit();
        this.createdDate = post.getCreatedDate();
        this.lastModifiedDate = post.getLastModifiedDate();
    }
}
