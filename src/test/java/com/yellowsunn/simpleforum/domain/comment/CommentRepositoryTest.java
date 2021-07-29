package com.yellowsunn.simpleforum.domain.comment;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnableJpaAuditing
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TestEntityManager em;
    Posts post;

    @BeforeEach
    void setUp() {
        post = Posts.builder()
                .title("title")
                .content("content")
                .type(Type.General)
                .build();

        em.persist(post);
    }

    @Test
    void saveAndFind() {
        //given
        Comment comment = Comment.builder()
                .content("content")
                .post(post)
                .build();

        commentRepository.save(comment);
        em.flush();
        em.clear();

        //when
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);

        //then
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo("content");
        assertThat(findComment.getCreatedDate()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("게시글을 지정하지 않으면 실패")
    void failNotIncludePosts() {
        //given
        Comment comment = Comment.builder()
                .content("content")
                .build();

        //then
        assertThatThrownBy(() -> commentRepository.saveAndFlush(comment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("부모 댓글 찾기")
    void findParentComment() {
        //given
        Comment parentComment = Comment.builder()
                .content("parent content")
                .post(post)
                .build();

        Comment childComment = Comment.builder()
                .content("child content")
                .parent(parentComment)
                .post(post)
                .build();

        commentRepository.save(parentComment);
        commentRepository.save(childComment);
        em.flush();
        em.clear();

        //when
        Comment findChildComment = commentRepository.findById(childComment.getId())
                .orElse(null);

        //then
        assertThat(findChildComment).isNotNull();
        assertThat(findChildComment.getParent()).isNotNull();
        assertThat(findChildComment.getParent().getId())
                .isEqualTo(parentComment.getId());
        assertThat(findChildComment.getParent().getContent())
                .isEqualTo(parentComment.getContent());
    }
}