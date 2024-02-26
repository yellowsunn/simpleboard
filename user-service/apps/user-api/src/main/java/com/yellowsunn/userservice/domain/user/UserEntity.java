package com.yellowsunn.userservice.domain.user;

import com.yellowsunn.userservice.domain.BaseTimeEntity;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import com.yellowsunn.userservice.dto.UserCreate;
import com.yellowsunn.userservice.dto.UserUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String userId;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder(access = AccessLevel.PRIVATE)
    private UserEntity(Long id, String nickName, String email, String password, String userId, String thumbnail,
            UserRole role) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.role = role;
    }

    @Builder(builderMethodName = "emailUserBuilder", builderClassName = "EmailUserBuilder")
    private UserEntity(@NonNull String email,
            @NonNull String password,
            @NonNull String nickName,
            String thumbnail) {
        this.email = email;
        this.password = password;
        this.userId = UUID.randomUUID().toString();
        this.nickName = nickName;
        this.role = UserRole.ROLE_USER;
        this.thumbnail = thumbnail;
    }

    @Builder(builderMethodName = "oauth2UserBuilder", builderClassName = "OAut2UserBuilder")
    private UserEntity(@NonNull String email,
            @NonNull String nickName,
            String thumbnail) {
        this.email = email;
        this.userId = UUID.randomUUID().toString();
        this.nickName = nickName;
        this.role = UserRole.ROLE_USER;
        this.thumbnail = thumbnail;
    }

    public static UserEntity create(UserCreate userCreate) {
        return UserEntity.builder()
                .email(userCreate.email())
                .nickName(userCreate.nickName())
                .password(userCreate.password())
                .userId(userCreate.userId())
                .thumbnail(userCreate.thumbnail())
                .role(userCreate.role())
                .build();
    }

    public boolean changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return true;
    }

    public boolean changeNickName(String nickName) {
        this.nickName = nickName;
        return true;
    }

    public User toUser(List<UserProviderEntity> userProviderEntities) {
        List<UserProvider> userProviders = userProviderEntities.stream()
                .map(UserProviderEntity::toUserProvider)
                .toList();

        return User.builder()
                .userId(userId)
                .email(email)
                .nickName(nickName)
                .password(password)
                .thumbnail(thumbnail)
                .userProviders(userProviders)
                .role(role)
                .build();
    }

    public void update(UserUpdate userUpdate) {
        this.nickName = userUpdate.nickName();
        this.password = userUpdate.password();
        this.thumbnail = userUpdate.thumbnail();
    }
}
