package com.yellowsunn.userservice.application.port;

import com.yellowsunn.userservice.domain.user.TempUser;
import java.time.Duration;
import java.util.Optional;

public interface TempUserCacheRepository {

    void save(TempUser tempUser, Duration timeout);

    Optional<TempUser> findByTokenAndCsrfToken(String token, String csrfToken);

    void deleteByToken(String token);
}
