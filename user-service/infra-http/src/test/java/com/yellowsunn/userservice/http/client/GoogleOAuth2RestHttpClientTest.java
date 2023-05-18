package com.yellowsunn.userservice.http.client;

import com.yellowsunn.userservice.http.OAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GoogleOAuth2RestHttpClientTest extends RestHttpClientTest {
    GoogleOAuth2RestHttpClient httpClient;

    @BeforeEach
    void setUp() {
        this.httpClient = new GoogleOAuth2RestHttpClient(this.restTemplate);
    }

    @Test
    void findUserInfo() {
        // given
        String token = "1234";
        mockServer.expect(requestTo("https://oauth2.googleapis.com/tokeninfo?id_token=1234"))
                .andRespond(withSuccess(getTestResponse(), MediaType.APPLICATION_JSON));

        OAuth2UserInfo userInfo = httpClient.findUserInfo(token);

        assertThat(userInfo.email()).isEqualTo("user@example.com");
    }

    private String getTestResponse() {
        return """
                {
                  "iss": "https://accounts.google.com",
                  "azp": "32555350559.apps.googleusercontent.com",
                  "aud": "32555350559.apps.googleusercontent.com",
                  "sub": "111260650121185072906",
                  "hd": "google.com",
                  "email": "user@example.com",
                  "email_verified": "true",
                  "at_hash": "_LLKKivfvfme9eoQ3WcMIg",
                  "iat": "1650053185",
                  "exp": "1650056785",
                  "alg": "RS256",
                  "kid": "f1338ca26835863f671403941738a7b49e740fc0",
                  "typ": "JWT"
                }
                """;
    }
}
