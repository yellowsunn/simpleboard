package com.yellowsunn.simpleforum.domain.file;

import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TestEntityManager em;
    Posts post;

    @BeforeEach
    void setUp() {
        post = Posts.builder().title("title")
                .content("content")
                .ip("0.0.0.0")
                .type(Type.General)
                .build();

        em.persist(post);
    }

    @Test
    void saveAndFind() {
        String uuid = UUID.randomUUID().toString();
        File file = File.builder()
                .uploadName("uploadName")
                .downloadName(uuid)
                .post(post)
                .build();

        fileRepository.save(file);
        em.flush();
        em.clear();

        File findFile = fileRepository.findById(file.getId()).orElse(null);

        assertThat(findFile).isNotNull();
        assertThat(findFile.getUploadName()).isEqualTo(file.getUploadName());
        assertThat(findFile.getDownloadName()).isEqualTo(file.getDownloadName());
        assertThat(findFile.getPost()).isNotNull();
    }

    @Test
    @DisplayName("게시글을 지정하지 않으면 실패")
    void failNotIncludePosts() {
        //given
        String uuid = UUID.randomUUID().toString();
        File file = File.builder()
                .uploadName("uploadName")
                .downloadName(uuid)
                .build();

        //then
        assertThatThrownBy(() -> fileRepository.saveAndFlush(file))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}