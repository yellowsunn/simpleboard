package com.yellowsunn.simpleboard.boardservice.dto.posts;

import com.yellowsunn.simpleboard.boardservice.domain.post.PostType;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostsUploadDto {

    @NotNull
    private PostType type;

    @NotBlank
    @Size(max = 300)
    private String title;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private List<MultipartFile> imageFiles = new ArrayList<>();

    public Posts covertToPosts(User user) {
        return Posts.builder()
                .type(type)
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
