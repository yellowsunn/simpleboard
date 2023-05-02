package com.yellowsunn.simpleboard.domain.user.repository;

import com.yellowsunn.simpleboard.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepositoryCustom {

    Slice<User> findCursorBasedSlice(String username, Long cursor, Pageable pageable);

    long findCursorBasedTotal(String username);
}
