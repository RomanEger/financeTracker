package com.reger.application.service;

import com.reger.application.dto.RegisterRequest;
import com.reger.application.dto.UserResponse;
import com.reger.domain.entity.User;
import com.reger.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.userName())) {
            throw new IllegalStateException("Username is already taken: " + registerRequest.userName());
        }

        var user = User.create(
                registerRequest.userName(),
                passwordEncoder.encode(registerRequest.password())
        );

        var savedUser = userRepository.create(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponse findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map((user) -> new UserResponse(user.getId(), user.getUsername()))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}