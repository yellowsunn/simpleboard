package com.yellowsunn.userservice.repository;

import com.yellowsunn.userservice.domain.user.TempUser;

import java.time.Duration;

public interface TempUserCacheRepository {
    void save(TempUser tempUser, Duration timeout);

    TempUser findByEmail(String email);
}
