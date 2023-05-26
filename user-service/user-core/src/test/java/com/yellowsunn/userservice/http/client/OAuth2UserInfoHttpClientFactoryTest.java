package com.yellowsunn.userservice.http.client;

import com.yellowsunn.userservice.constant.OAuth2Type;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfoHttpClient;
import com.yellowsunn.userservice.http.oauth2.OAuth2UserInfoHttpClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OAuth2UserInfoHttpClientFactoryTest {
    OAuth2UserInfoHttpClient googleClient = mock(OAuth2UserInfoHttpClient.class);
    OAuth2UserInfoHttpClient naverClient = mock(OAuth2UserInfoHttpClient.class);

    OAuth2UserInfoHttpClientFactory factory;

    @BeforeEach
    void setUp() {
        given(googleClient.type()).willReturn(OAuth2Type.GOOGLE);
        given(naverClient.type()).willReturn(OAuth2Type.NAVER);

        this.factory = new OAuth2UserInfoHttpClientFactory(List.of(googleClient, naverClient));
    }

    @Test
    void get() {
        // when
        var client1 = factory.get(OAuth2Type.GOOGLE);
        var client2 = factory.get(OAuth2Type.NAVER);

        // then
        assertThat(client1).isSameAs(googleClient);
        assertThat(client2).isSameAs(naverClient);
    }
}
