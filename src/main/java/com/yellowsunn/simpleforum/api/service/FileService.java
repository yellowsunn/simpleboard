package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.file.FileUploadDto;
import com.yellowsunn.simpleforum.domain.file.File;
import com.yellowsunn.simpleforum.domain.file.FileRepository;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
