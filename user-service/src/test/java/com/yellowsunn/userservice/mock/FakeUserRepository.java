package com.yellowsunn.userservice.mock;

import com.yellowsunn.userservice.domain.dto.EmailUserInfoDto;
import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.port.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private List<User> data = new ArrayList<>();

    @Override
    public void save(User user) {
        data.add(user);
    }

    @Override
    public Optional<EmailUserInfoDto> findEmailUserInfo(String email) {
        return Optional.empty();
    }
}
