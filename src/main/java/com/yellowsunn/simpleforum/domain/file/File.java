package com.yellowsunn.simpleforum.domain.file;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class File {
    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @Builder
    public File(String storeName, Posts post) {
        this.storeName = storeName;
        this.post = post;
    }
}
