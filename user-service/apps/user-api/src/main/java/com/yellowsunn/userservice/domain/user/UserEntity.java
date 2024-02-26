package com.yellowsunn.userservice.domain.user;

import com.yellowsunn.userservice.domain.BaseTimeEntity;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    private String email;

    @Column(name = "nickname", unique = true)
    private String nickName;

    private String password;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder(access = AccessLevel.PRIVATE)
    private UserEntity(Long id, String email, String nickName, String password, String userId, String thumbnail,
            UserRole role) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.role = role;
    }

    public static UserEntity create(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .password(user.getPassword())
                .thumbnail(user.getThumbnail())
                .role(user.getRole())
                .build();
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

    public void update(User user) {
        this.nickName = user.getNickName();
        this.password = user.getPassword();
        this.thumbnail = user.getThumbnail();
    }
}
