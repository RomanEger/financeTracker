package com.reger.domain.repository;

import com.reger.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByUsername(String username);

    Optional<User> findByUsernameWithRoles(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    User create(User user);

    User update(User user);

    void delete(UUID id);
}