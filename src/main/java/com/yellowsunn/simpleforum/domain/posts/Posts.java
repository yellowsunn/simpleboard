package com.yellowsunn.simpleforum.domain.posts;

import com.yellowsunn.simpleforum.domain.BaseTimeEntity;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.file.File;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    private long hit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> imageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Posts(String title, String content, Type type, User user) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.user = user;
    }
}
