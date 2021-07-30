package com.yellowsunn.simpleforum.api.dto.posts;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostsUploadDto {

    @NotNull
    private PostType type;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<MultipartFile> imageFiles;

    public Posts covertToPosts(User user) {
        return Posts.builder()
                .type(type)
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
