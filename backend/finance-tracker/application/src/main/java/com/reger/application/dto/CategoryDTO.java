package com.reger.application.dto;

import com.reger.domain.entity.Category;

import java.util.UUID;

public record CategoryDTO(UUID id, String categoryName) {
    public static CategoryDTO from(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}