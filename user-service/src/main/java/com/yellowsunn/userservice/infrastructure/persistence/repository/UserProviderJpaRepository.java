package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.domain.vo.Provider;
import com.yellowsunn.userservice.infrastructure.persistence.entity.UserProviderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProviderJpaRepository extends JpaRepository<UserProviderEntity, Long> {

    Optional<UserProviderEntity> findByEmailAndProvider(String email, Provider provider);
}
