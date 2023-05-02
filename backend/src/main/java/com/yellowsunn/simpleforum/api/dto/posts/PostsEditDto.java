package com.yellowsunn.simpleforum.api.dto.posts;

import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostsEditDto {

    @NotNull
    private PostType type;

    @NotBlank
    @Size(max = 300)
    private String title;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private List<MultipartFile> imageFiles = new ArrayList<>();

    public void editPost(Posts post) {
        post.changeType(type);
        post.changeTitle(title);
        post.changeContent(content);
    }
}
