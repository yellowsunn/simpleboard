package com.yellowsunn.userservice.http.client;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public abstract class RestHttpClientTest {
    protected MockRestServiceServer mockServer;
    protected RestTemplate restTemplate;

    public RestHttpClientTest() {
        this.restTemplate = new RestTemplate();
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }
}
