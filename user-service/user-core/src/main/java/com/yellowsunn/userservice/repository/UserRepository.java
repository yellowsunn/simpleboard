package com.yellowsunn.userservice.repository;

import com.yellowsunn.userservice.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    User save(User entity);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean delete(User entity);

    boolean existsByNickName(String nickName);

    Optional<User> findByUUID(String uuid);

    Optional<User> findByNickName(String nickName);
}
