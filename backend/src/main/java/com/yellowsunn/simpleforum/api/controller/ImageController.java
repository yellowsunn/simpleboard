package com.yellowsunn.simpleforum.api.controller;

import com.yellowsunn.simpleforum.api.util.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {

    private final FileStore fileStore;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) throws IOException {
        Resource resource = new UrlResource("file:" + fileStore.getFullPath(fileName));
        if (!resource.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(7L)))
                .lastModified(resource.lastModified())
                .contentType(fileStore.getImageContentType(resource.getFilename()))
                .body(resource);
    }
}
