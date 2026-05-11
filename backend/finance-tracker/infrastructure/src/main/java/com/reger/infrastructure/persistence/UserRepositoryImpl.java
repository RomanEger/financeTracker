package com.reger.infrastructure.persistence;

import com.reger.domain.entity.User;
import com.reger.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameWithRoles(String username) {
        return userJpaRepository.findByUsernameWithRoles(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public User create(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }
}