package com.yellowsunn.simpleforum.domain.posts;

import com.yellowsunn.simpleforum.api.service.PostsService;
import com.yellowsunn.simpleforum.domain.comment.Comment;
import com.yellowsunn.simpleforum.domain.file.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    TestEntityManager em;

    @Test
    void saveAndFind() {
        //given
        Posts post = getSamplePost();

        postsRepository.save(post);
        em.flush();
        em.clear();

        //when
        Posts findPost = postsRepository.findById(post.getId())
                .orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
        assertThat(findPost.getType()).isEqualTo(post.getType());
        assertThat(findPost.getHit()).isEqualTo(0L);
        assertThat(findPost.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(findPost.getLastModifiedDate()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("게시글 삭제시 게시글 내의 댓글, 파일 모두 삭제")
    void deletePost() {
        //given
        Posts post = getSamplePost();

        Comment comment = Comment.builder()
                .content("content")
                .post(post)
                .build();

        File file = File.builder()
                .uploadName("uploadName")
                .storeName("downloadName")
                .post(post)
                .build();

        postsRepository.save(post);
        em.persist(comment);
        em.persist(file);

        em.flush();
        em.clear();

        //when
        postsRepository.findById(post.getId())
                .ifPresent(findPost -> postsRepository.delete(findPost));

        //then
        List<Posts> findPosts = postsRepository.findAll();
        assertThat(findPosts.size()).isEqualTo(0);
        assertThat(em.find(Comment.class, comment.getId())).isNull();
        assertThat(em.find(File.class, file.getId())).isNull();
    }

    @DisplayName("게시글 조회 및 조회수 증가")
    @Test
    void findPostAndUpdateHit() {
        //given
        Posts post = getSamplePost();
        em.persistAndFlush(post);
        em.clear();

        //given
        Posts findPost = postsRepository.findPostAndUpdateHit(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getHit()).isEqualTo(1L);
    }

    Posts getSamplePost() {
        return Posts.builder()
                .title("title")
                .content("content")
                .type(PostType.GENERAL)
                .build();
    }
}