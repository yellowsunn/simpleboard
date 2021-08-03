package com.yellowsunn.simpleforum.domain.postHit;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PostHit {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    private Posts post;

    private long hit;

    public PostHit(Posts post) {
        this.post = post;
    }

    public void updateHit() {
        hit += 1;
    }
}
