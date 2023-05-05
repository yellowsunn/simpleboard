package com.yellowsunn.simpleboard.boardservice.util;

import com.yellowsunn.simpleboard.boardservice.dto.file.FileUploadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<FileUploadDto> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileUploadDto> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    public FileUploadDto storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String storeFileName = multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return FileUploadDto.builder()
                .storeFileName(storeFileName)
                .build();
    }

    public MediaType getImageContentType(String fileName) {
        String ext = extractExt(fileName);
        if ("gif".equalsIgnoreCase(ext)) return MediaType.IMAGE_GIF;
        else if ("png".equalsIgnoreCase(ext)) return MediaType.IMAGE_PNG;
        else return MediaType.IMAGE_JPEG;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
