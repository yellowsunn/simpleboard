package com.yellowsunn.simpleboard.domain.posts;

import com.yellowsunn.simpleboard.api.dto.posts.PostsGetAllDto;
import com.yellowsunn.simpleboard.domain.comment.Comment;
import com.yellowsunn.simpleboard.domain.file.File;
import com.yellowsunn.simpleboard.domain.postHit.PostHit;
import com.yellowsunn.simpleboard.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleboard.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @DisplayName("게시글 삭제")
    void deletePost() {
        //given
        Posts post = getSamplePost();

        postsRepository.save(post);

        em.flush();
        em.clear();

        //when
        postsRepository.findById(post.getId())
                .ifPresent(findPost -> postsRepository.delete(findPost));

        //then
        List<Posts> findPosts = postsRepository.findAll();
        assertThat(findPosts.size()).isEqualTo(0);
    }

    @DisplayName("게시글 조회")
    @Test
    void findPostAndUpdateHit() {
        //given
        Posts post = getSamplePost();
        em.persistAndFlush(post);
        em.clear();

        //given
        Posts findPost = postsRepository.findPost(post.getId()).orElse(null);

        //then
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo("title");
        assertThat(findPost.getContent()).isEqualTo("content");
    }

    @Test
    void findCustomAll() {

        //given
        User user = User.builder().username("username").password("password").build();
        Posts post = Posts.builder().title("title").content("content").user(user).type(PostType.GENERAL).build();
        PostHit postHit = new PostHit(post);
        post.updateHit();
        em.persist(user);
        em.persist(post);
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder().content("content" + i).user(user).post(post).build();
            em.persist(comment);
        }
        File imageFile = File.builder().post(post).storeName("storeName").build();
        em.persist(imageFile);

        //when
        Page<PostsGetAllDto> page = postsRepository.findDtoAll(PageRequest.of(0, 30), "title", "username");

        //then
        PostsGetAllDto dto = page.getContent().get(0);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(dto.getHit()).isEqualTo(1);
        assertThat(dto.getCommentSize()).isEqualTo(5);
        assertThat(dto.isHasImage()).isTrue();
        assertThat(dto.getUsername()).isEqualTo("username");
    }

    Posts getSamplePost() {
        return Posts.builder()
                .title("title")
                .content("content")
                .type(PostType.GENERAL)
                .build();
    }
}
