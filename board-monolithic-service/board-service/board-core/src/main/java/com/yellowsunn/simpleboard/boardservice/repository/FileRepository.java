package com.yellowsunn.simpleboard.boardservice.repository;

import com.yellowsunn.simpleboard.boardservice.domain.file.File;
import com.yellowsunn.simpleboard.boardservice.domain.post.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    void deleteAllByPost(Posts post);
}
