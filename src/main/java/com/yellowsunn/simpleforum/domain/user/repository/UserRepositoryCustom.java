package com.yellowsunn.simpleforum.domain.user.repository;

import com.yellowsunn.simpleforum.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepositoryCustom {

    Slice<User> findCursorBasedSlice(String username, Long cursor, Pageable pageable);
}
