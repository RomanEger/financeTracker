package com.reger.infrastructure.persistence;

import com.reger.domain.entity.Category;
import com.reger.domain.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final EntityManager entityManager;

    @Override
    public List<Category> findAll(String name) {
        if (name == null) {
            return categoryJpaRepository.findAll();
        }

        return categoryJpaRepository.findByNameContainsIgnoreCase(name);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public List<Category> findAllById(List<UUID> ids) {
        return categoryJpaRepository.findAllById(ids);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Category create(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Category update(UUID id, String newName) {
        var category = entityManager.getReference(Category.class, id);
        category.setName(newName);

        return categoryJpaRepository.save(category);
    }

    @Override
    public void delete(UUID id) {
        categoryJpaRepository.deleteById(id);
    }
}