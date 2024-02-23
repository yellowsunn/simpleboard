package com.yellowsunn.userservice.http.oauth2;

import com.yellowsunn.userservice.constant.OAuth2Type;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuth2UserInfoHttpClientFactory {
    private final Map<OAuth2Type, OAuth2UserInfoHttpClient> clientMap;

    public OAuth2UserInfoHttpClientFactory(Collection<OAuth2UserInfoHttpClient> clients) {
        this.clientMap = clients.stream()
                .collect(Collectors.toMap(OAuth2UserInfoHttpClient::type, Function.identity()));
    }

    public OAuth2UserInfoHttpClient get(OAuth2Type type) {
        var httpClient = clientMap.get(type);
        Assert.notNull(httpClient, "Unsupported type. type=" + type);

        return httpClient;
    }
}
