package com.yellowsunn.userservice.infrastructure.persistence.entity;

import com.yellowsunn.userservice.domain.dto.UserProviderCreate;
import com.yellowsunn.userservice.domain.vo.Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_provider")
public class UserProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String email;

    private String password;

    @Builder
    private UserProviderEntity(long id, String userId, Provider provider, String email, String password) {
        this.id = id;
        this.userId = userId;
        this.provider = provider;
        this.email = email;
        this.password = password;
    }

    public static List<UserProviderEntity> creates(List<UserProviderCreate> userProviderCreates) {
        if (CollectionUtils.isEmpty(userProviderCreates)) {
            return Collections.emptyList();
        }

        return userProviderCreates.stream()
                .map(it -> UserProviderEntity.builder()
                        .userId(it.userId())
                        .provider(it.provider())
                        .email(it.email())
                        .password(it.password())
                        .build())
                .toList();
    }
}
