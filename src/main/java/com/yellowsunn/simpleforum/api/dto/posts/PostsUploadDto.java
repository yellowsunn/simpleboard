package com.yellowsunn.simpleforum.api.dto.posts;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.Type;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostsUploadDto {

    private Type type;
    private String title;
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
