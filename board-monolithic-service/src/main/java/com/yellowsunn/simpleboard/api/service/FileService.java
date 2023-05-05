package com.yellowsunn.simpleboard.api.service;

import com.yellowsunn.simpleboard.domain.posts.Posts;
import com.yellowsunn.simpleboard.api.dto.file.FileUploadDto;
import com.yellowsunn.simpleboard.domain.file.File;
import com.yellowsunn.simpleboard.domain.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void storeAndSaveAll(Posts post, List<FileUploadDto> fileUploadDtos) {
        List<File> files = fileUploadDtos.stream()
                .map(dto -> dto.convertToFileEntity(post))
                .collect(Collectors.toList());

        fileRepository.saveAll(files);
    }
}
