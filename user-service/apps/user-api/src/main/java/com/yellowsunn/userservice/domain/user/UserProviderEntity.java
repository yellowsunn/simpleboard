package com.yellowsunn.userservice.domain.user;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_provider")
@Entity
public class UserProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String providerEmail;

    @Builder
    public UserProviderEntity(
            String userId,
            Provider provider,
            String providerEmail
    ) {
        this.userId = userId;
        this.provider = provider;
        this.providerEmail = providerEmail;
    }

    public static List<UserProviderEntity> creates(String userId, List<UserProvider> userProviders) {
        return emptyIfNull(userProviders).stream()
                .map(it -> UserProviderEntity.builder()
                        .userId(userId)
                        .provider(it.getProvider())
                        .providerEmail(it.getEmail())
                        .build()
                ).toList();
    }

    public UserProvider toUserProvider() {
        return UserProvider.builder()
                .email(providerEmail)
                .provider(provider)
                .build();
    }
}
