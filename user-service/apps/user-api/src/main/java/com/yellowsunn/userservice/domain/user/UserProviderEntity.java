package com.yellowsunn.userservice.domain.user;

import com.yellowsunn.userservice.domain.vo.UserProvider;
import com.yellowsunn.userservice.dto.UserProviderCreate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_provider")
@Entity
public class UserProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_provider_id")
    private Long id;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
    @Transient
    private UserEntity userEntity;

    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String providerEmail;

    @Builder
    public UserProviderEntity(
            @NonNull UserEntity userEntity,
            String userId,
            @NonNull Provider provider,
            @NonNull String providerEmail
    ) {
        this.userEntity = userEntity;
        this.userId = userId;
        this.provider = provider;
        this.providerEmail = providerEmail;
    }

    public static UserProviderEntity create(UserProviderCreate userProviderCreate) {
        return UserProviderEntity.builder()
                .userId(userProviderCreate.userId())
                .provider(userProviderCreate.provider())
                .providerEmail(userProviderCreate.email())
                .build();
    }

    public static List<UserProviderEntity> creates(List<UserProviderCreate> userProviderCreates) {
        return userProviderCreates.stream()
                .map(UserProviderEntity::create)
                .toList();
    }

    public UserProvider toUserProvider() {
        return UserProvider.builder()
                .email(providerEmail)
                .provider(provider)
                .build();
    }
}
