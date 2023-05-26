package com.yellowsunn.userservice.http.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.URI;

@Component
public class ImageRestHttpClient implements ImageHttpClient {
    private final RestTemplate restTemplate;
    private final String imgServiceUrl;

    public ImageRestHttpClient(RestTemplate restTemplate,
                               @Value("${micro-services.image-service-url}") String imgServiceUrl) {
        this.restTemplate = restTemplate;
        this.imgServiceUrl = imgServiceUrl;
    }

    @Override
    public String uploadThumbnailImage(String originalFilename, InputStream inputStream) {
        var resource = new MultipartInputStreamFileResource(originalFilename, inputStream);
        var map = new LinkedMultiValueMap<String, Object>();
        map.add("image", resource);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        var httpEntity = new HttpEntity<>(map, headers);

        URI uri = UriComponentsBuilder.fromUriString(imgServiceUrl)
                .path("/api/internal/images/thumbnail")
                .build()
                .toUri();

        return restTemplate.postForObject(uri, httpEntity, String.class);
    }
}
