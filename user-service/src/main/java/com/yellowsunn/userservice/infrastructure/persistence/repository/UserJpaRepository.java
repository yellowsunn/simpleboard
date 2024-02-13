package com.yellowsunn.userservice.infrastructure.persistence.repository;

import com.yellowsunn.userservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

}
