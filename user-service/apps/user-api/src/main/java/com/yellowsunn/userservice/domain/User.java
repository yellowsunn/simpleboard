package com.yellowsunn.userservice.domain;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserRole;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import com.yellowsunn.userservice.exception.CustomUserException;
import com.yellowsunn.userservice.exception.UserErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@Getter
public class User {

    private String userId;
    private String email;
    private String nickName;
    private String password;
    private String thumbnail;
    private UserRole role;

    @Getter
    private List<UserProvider> userProviders;

    @Builder
    public User(String userId, String email, String nickName, String password, String thumbnail, UserRole role,
            List<UserProvider> userProviders) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.thumbnail = thumbnail;
        this.role = role;
        this.userProviders = new ArrayList<>(emptyIfNull(userProviders));
    }

    public static User createEmailUser(String userId, String email, String nickName, String password) {
        return User.builder()
                .userId(userId)
                .email(email)
                .nickName(nickName)
                .password(password)
                .role(UserRole.ROLE_USER)
                .userProviders(List.of(UserProvider.createEmailProvider(email)))
                .build();
    }

    public static User createOAuth2User(String userId, String email, String nickName, String thumbnail,
            Provider provider) {

        return User.builder()
                .userId(userId)
                .email(email)
                .nickName(nickName)
                .thumbnail(thumbnail)
                .role(UserRole.ROLE_USER)
                .userProviders(List.of(UserProvider.createOAuth2Provider(email, provider)))
                .build();
    }

    public List<Provider> providers() {
        return userProviders.stream()
                .map(UserProvider::getProvider)
                .toList();
    }

    public boolean isNotSameUser(String userId) {
        return !StringUtils.equals(this.userId, userId);
    }

    public void addUserProvider(UserProvider userProvider) {
        if (userProviders.contains(userProvider)) {
            return;
        }
        userProviders.add(userProvider);
    }

    public void deleteProvider(Provider provider) {
        if (userProviders.size() <= 1) {
            throw new CustomUserException(UserErrorCode.LINK_AT_LEAST_ONE_USER_PROVIDER);
        }
        userProviders.removeIf(it -> Objects.equals(it.getProvider(), provider));
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeNickName(String nickName) {
        Assert.hasText(nickName, () -> "nickName must not be blank.");
        this.nickName = nickName;
    }
}
