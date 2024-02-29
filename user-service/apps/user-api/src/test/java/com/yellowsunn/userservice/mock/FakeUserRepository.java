package com.yellowsunn.userservice.mock;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import com.google.common.base.Predicate;
import com.yellowsunn.common.exception.UserNotFoundException;
import com.yellowsunn.userservice.application.port.UserRepository;
import com.yellowsunn.userservice.domain.User;
import com.yellowsunn.userservice.domain.user.Provider;
import com.yellowsunn.userservice.domain.vo.UserProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class FakeUserRepository implements UserRepository {

    private final Map<String, User> data = new HashMap<>();

    @Override
    public void insert(User user) {
        data.put(user.getUserId(), user);
    }

    @Override
    public void update(User user) {
        data.replace(user.getUserId(), user);
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUserId());
    }

    @Override
    public Optional<User> findByEmailAndProvider(String email, Provider provider) {
        Predicate<List<UserProvider>> isMatched = (userProviders) -> userProviders.stream()
                .filter(userProvider -> StringUtils.equals(userProvider.getEmail(), email))
                .anyMatch(userProvider -> Objects.equals(userProvider.getProvider(), provider));

        return data.values().stream()
                .filter(it -> isMatched.apply(it.getUserProviders()))
                .findAny();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public Optional<User> findByNickName(String nickName) {
        return data.values().stream()
                .filter(it -> StringUtils.equals(it.getNickName(), nickName))
                .findAny();
    }

    @Override
    public List<User> findByUserIds(List<String> userIds) {
        return data.values().stream()
                .filter(it -> emptyIfNull(userIds).contains(it.getUserId()))
                .toList();
    }

    @Override
    public User getByUserId(String userId) {
        return findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean existsByEmailAndProvider(String email, Provider provider) {
        return data.values().stream()
                .map(User::getUserProviders)
                .flatMap(Collection::stream)
                .filter(userProvider -> StringUtils.equals(userProvider.getEmail(), email))
                .anyMatch(userProvider -> Objects.equals(userProvider.getProvider(), provider));
    }

    @Override
    public boolean existsByNickName(String nickName) {
        return data.values().stream()
                .anyMatch(it -> StringUtils.equals(it.getNickName(), nickName));
    }
}
