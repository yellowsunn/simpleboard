package com.yellowsunn.userservice.application.port;

import com.yellowsunn.userservice.domain.user.TempUser;
import java.time.Duration;

public interface TempUserCacheRepository {

    void save(TempUser tempUser, Duration timeout);

    TempUser findByTokenAndCsrfToken(String token, String csrfToken);

    boolean deleteByToken(String token);
}
