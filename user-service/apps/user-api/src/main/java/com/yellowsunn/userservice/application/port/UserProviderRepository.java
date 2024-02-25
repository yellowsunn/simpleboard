package com.yellowsunn.userservice.application.port;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserProvider;
import java.util.List;
import java.util.Optional;

public interface UserProviderRepository {

    UserProvider save(UserProvider entity);

    boolean existsByUserIdAndProvider(Long userId, Provider provider);

    Optional<UserProvider> findByProviderEmailAndProvider(String providerEmail, Provider provider);

    List<Provider> findProvidersByUserId(Long userId);

    boolean deleteByUserIdAndProvider(Long userId, Provider provider);

    long countProvidersByUserId(Long userId);

    boolean deleteByUserId(Long userId);
}
