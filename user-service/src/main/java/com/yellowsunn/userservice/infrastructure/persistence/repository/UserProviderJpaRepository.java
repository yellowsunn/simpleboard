package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.infrastructure.persistence.entity.UserProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProviderJpaRepository extends JpaRepository<UserProviderEntity, Long> {

}
