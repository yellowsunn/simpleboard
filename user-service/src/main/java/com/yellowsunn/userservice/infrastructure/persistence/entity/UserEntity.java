package com.yellowsunn.userservice.infrastructure.persistence.entity;

import com.yellowsunn.userservice.domain.dto.UserCreate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;

    private String nickname;

    private String thumbnail;

    @Builder
    private UserEntity(long id, String userId, String nickname, String thumbnail) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.thumbnail = thumbnail;
    }

    public static UserEntity create(UserCreate userCreate) {
        return UserEntity.builder()
                .userId(userCreate.userId())
                .nickname(userCreate.nickname())
                .thumbnail(userCreate.thumbnail())
                .build();
    }
}
