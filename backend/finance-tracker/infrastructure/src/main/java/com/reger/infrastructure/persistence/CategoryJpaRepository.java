package com.reger.infrastructure.persistence;

import com.reger.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<Category, UUID> {

    List<Category> findByNameContainsIgnoreCase(String categoryName);

    boolean existsByNameIgnoreCase(String name);
}