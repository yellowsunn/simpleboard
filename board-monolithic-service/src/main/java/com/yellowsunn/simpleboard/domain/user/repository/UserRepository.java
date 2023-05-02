package com.yellowsunn.simpleboard.domain.user.repository;

import com.yellowsunn.simpleboard.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);
}
