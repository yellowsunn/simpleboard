package com.yellowsunn.simpleforum.api.dto.posts;

import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostsEditDto {

    @NotNull
    private PostType type;

    @NotBlank
    @Size(max = 512)
    private String title;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private List<MultipartFile> imageFiles;

    public void editPost(Posts post) {
        post.changeType(type);
        post.changeTitle(title);
        post.changeContent(content);
    }
}
