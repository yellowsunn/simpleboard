package com.yellowsunn.userservice.repository;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserProvider;

import java.util.Optional;

public interface UserProviderRepository {
    UserProvider save(UserProvider entity);

    boolean existsByUserIdAndProvider(Long userId, Provider provider);

    Optional<UserProvider> findByProviderEmailAndProvider(String providerEmail, Provider provider);
}
