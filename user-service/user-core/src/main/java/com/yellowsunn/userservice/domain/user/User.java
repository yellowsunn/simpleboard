package com.yellowsunn.userservice.domain.user;

import com.yellowsunn.userservice.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String uuid;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder(builderMethodName = "emailUserBuilder", builderClassName = "EmailUserBuilder")
    private User(@NonNull String email,
                 @NonNull String password,
                 @NonNull String nickName,
                 String thumbnail) {
        this.email = email;
        this.password = password;
        this.uuid = UUID.randomUUID().toString();
        this.nickName = nickName;
        this.role = UserRole.ROLE_USER;
        this.thumbnail = thumbnail;
    }

    @Builder(builderMethodName = "oauth2UserBuilder", builderClassName = "OAut2UserBuilder")
    private User(@NonNull String email,
                 @NonNull String nickName,
                 String thumbnail) {
        this.email = email;
        this.uuid = UUID.randomUUID().toString();
        this.nickName = nickName;
        this.role = UserRole.ROLE_USER;
        this.thumbnail = thumbnail;
    }

    public boolean changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return true;
    }
}
