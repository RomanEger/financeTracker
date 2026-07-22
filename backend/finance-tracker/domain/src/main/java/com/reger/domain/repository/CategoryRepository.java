package com.reger.domain.repository;

import com.reger.domain.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    List<Category> findAll(String name);

    Optional<Category> findById(UUID id);

    List<Category> findAllById(List<UUID> ids);

    boolean existsByName(String name);

    Category create(Category category);

    Category update(UUID id, String newName);

    void delete(UUID id);
}