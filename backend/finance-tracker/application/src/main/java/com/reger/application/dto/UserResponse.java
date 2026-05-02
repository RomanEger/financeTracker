package com.reger.application.dto;

import java.util.UUID;

public record UserResponse(UUID id, String username) {
}