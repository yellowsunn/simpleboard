package com.yellowsunn.userservice.domain.comment;

import com.yellowsunn.simpleboard.domain.comment.Comment;
import com.yellowsunn.simpleboard.domain.comment.repository.CommentRepository;
import com.yellowsunn.simpleboard.domain.posts.PostType;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .type(PostType.GENERAL)
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

    @Test
    void findByPostId() {
        //given
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            Comment saveComment = commentRepository.save(Comment.builder().content("content" + i).post(post).build());
            comments.add(saveComment);
        }
        commentRepository.save(Comment.builder().content("content47").parent(comments.get(20)).post(post).build());
        commentRepository.save(Comment.builder().content("content48").parent(comments.get(20)).post(post).build());
        commentRepository.save(Comment.builder().content("content49").parent(comments.get(23)).post(post).build());

        //when
        Slice<Comment> commentPage = commentRepository.findCursorBasedSliceByPostId(post.getId(), String.format("%020d", comments.get(19).getParent().getId()) + String.format("%020d", comments.get(19).getId()), PageRequest.of(0, 20));

        //then
        assertThat(commentPage.hasNext()).isTrue();
        assertThat(commentPage.getContent().get(0).getContent()).isEqualTo("content20");
        assertThat(commentPage.getContent().get(1).getContent()).isEqualTo("content47");
        assertThat(commentPage.getContent().get(2).getContent()).isEqualTo("content48");
        assertThat(commentPage.getContent().get(5).getContent()).isEqualTo("content23");
        assertThat(commentPage.getContent().get(6).getContent()).isEqualTo("content49");
    }
}
