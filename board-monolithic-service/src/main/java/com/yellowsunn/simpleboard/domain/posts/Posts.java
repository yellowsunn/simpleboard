package com.yellowsunn.simpleboard.domain.posts;

import com.yellowsunn.simpleboard.domain.BaseTimeEntity;
import com.yellowsunn.simpleboard.domain.comment.Comment;
import com.yellowsunn.simpleboard.domain.file.File;
import com.yellowsunn.simpleboard.domain.postHit.PostHit;
import com.yellowsunn.simpleboard.userservice.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PostType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostHit hit;

    @OneToMany(mappedBy = "post")
    private List<File> imageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Posts(String title, String content, PostType type, User user) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.user = user;
        this.hit = new PostHit(this);
    }

    public Long getHit() {
        return hit != null ? hit.getHit() : null;
    }

    public void updateHit() {
        hit.updateHit();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeType(PostType type) {
        this.type = type;
    }
}
