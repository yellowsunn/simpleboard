package com.yellowsunn.userservice.application.port;

import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void insert(User user);

    void update(User user);

    void delete(User user);

    Optional<User> findByEmailAndProvider(String email, Provider provider);

    Optional<User> findByUserId(String userId);

    Optional<User> findByNickName(String nickName);

    List<User> findByUserIds(List<String> userIds);

    User getByUserId(String userId);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickName(String nickName);
}
