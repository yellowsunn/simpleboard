package com.yellowsunn.userservice.infrastructure.persistence;

import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.user.UserProviderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProviderJpaRepository extends JpaRepository<UserProviderEntity, Long> {

    Optional<UserProviderEntity> findByProviderAndProviderEmail(Provider provider, String providerEmail);

    List<UserProviderEntity> findByUserId(String userId);

    List<UserProviderEntity> findByUserIdIn(List<String> userIds);
}
