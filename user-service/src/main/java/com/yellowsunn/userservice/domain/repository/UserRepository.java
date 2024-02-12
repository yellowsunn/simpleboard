package com.yellowsunn.userservice.domain.repository;

import com.yellowsunn.userservice.domain.entity.User;

public interface UserRepository {

    void save(User user);
}
