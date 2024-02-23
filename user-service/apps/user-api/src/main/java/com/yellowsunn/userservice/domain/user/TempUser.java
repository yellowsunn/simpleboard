package com.yellowsunn.userservice.domain.user;

import com.yellowsunn.common.utils.Base64Handler;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class TempUser {
    private final String email;
    private final Provider provider;
    private final String thumbnail;
    private final String token;
    private final String csrfToken;

    @Builder
    @ConstructorProperties({"email", "provider", "thumbnail", "csrfToken"})
    private TempUser(String email,
                     Provider provider,
                     String thumbnail,
                     String csrfToken) {
        this.email = email;
        this.provider = provider;
        this.thumbnail = thumbnail;
        this.token = generateBase64UUid();
        this.csrfToken = csrfToken;
    }

    private String generateBase64UUid() {
        String uuid = UUID.randomUUID().toString();
        return Base64Handler.encode(uuid);
    }
}
