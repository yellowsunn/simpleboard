package com.yellowsunn.simpleboard.userservice.repository;

import com.yellowsunn.simpleboard.userservice.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long aLong);

    Optional<User> findByUsername(String username);

    Slice<User> findCursorBasedSlice(String username, Long cursor, Pageable pageable);

    long findCursorBasedTotal(String username);
}
