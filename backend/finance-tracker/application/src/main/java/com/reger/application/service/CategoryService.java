package com.reger.application.service;

import com.reger.application.NotFoundException;
import com.reger.application.dto.CategoryCreateDTO;
import com.reger.application.dto.CategoryDTO;
import com.reger.application.dto.CategoryUpdateDTO;
import com.reger.domain.entity.Category;
import com.reger.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAll(String name) {
        return categoryRepository.findAll(name)
                .stream()
                .map(CategoryDTO::from)
                .toList();
    }

    public CategoryDTO findById(UUID categoryId) throws NotFoundException {
        return categoryRepository.findById(categoryId)
                .map(CategoryDTO::from)
                .orElseThrow(() -> new NotFoundException("Category not found. Id: " + categoryId));
    }

    public CategoryDTO create(CategoryCreateDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.categoryName())) {
            throw new IllegalStateException("Category with name " + categoryDTO.categoryName() + " already exists");
        }

        var category = Category.create(categoryDTO.categoryName());
        var newCategory = categoryRepository.create(category);

        return CategoryDTO.from(newCategory);
    }

    public CategoryDTO update(CategoryUpdateDTO categoryDTO) {
        var updatedCategory = categoryRepository.update(categoryDTO.id(), categoryDTO.newCategoryName());

        return CategoryDTO.from(updatedCategory);
    }

    public void deleteById(UUID categoryId) {
        categoryRepository.delete(categoryId);
    }
}