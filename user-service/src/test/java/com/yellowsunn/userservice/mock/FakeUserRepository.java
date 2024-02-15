package com.yellowsunn.userservice.mock;

import com.yellowsunn.userservice.domain.entity.User;
import com.yellowsunn.userservice.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {

    private List<User> data = new ArrayList<>();

    @Override
    public void save(User user) {
        data.add(user);
    }
}
