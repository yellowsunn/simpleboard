package com.yellowsunn.simpleboard.domain.file;

import com.yellowsunn.simpleboard.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    void deleteAllByPost(Posts post);
}
