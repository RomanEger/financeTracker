package com.reger.application.dto;

import java.util.UUID;

public record CategoryUpdateDTO(UUID id, String newCategoryName) {
}