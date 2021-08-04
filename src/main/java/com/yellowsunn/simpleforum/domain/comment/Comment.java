package com.yellowsunn.simpleforum.domain.comment;

import com.yellowsunn.simpleforum.domain.BaseCreatedTimeEntity;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseCreatedTimeEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @Builder
    public Comment(String content, Comment parent, User user, Posts post) {
        this.content = content;
        this.parent = parent;
        this.user = user;
        this.post = post;
    }
}
