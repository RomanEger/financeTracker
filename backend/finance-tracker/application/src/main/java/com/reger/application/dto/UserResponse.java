package com.reger.application.dto;

import com.reger.domain.entity.User;

import java.util.UUID;

public record UserResponse(UUID id, String username) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}