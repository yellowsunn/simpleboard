package com.yellowsunn.userservice.oauth2;

import com.yellowsunn.userservice.domain.user.Provider;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attribute {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    private final String picture;
    private final Provider provider;

    @Builder
    private OAuth2Attribute(Map<String, Object> attributes, String nameAttributeKey, String email, String picture, Provider provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
    }

    public static OAuth2Attribute of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuth2Attribute ofGoogle(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .provider(Provider.GOOGLE)
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
