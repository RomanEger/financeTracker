package com.reger.presentation.controller;

import com.reger.application.NotFoundException;
import com.reger.application.dto.CategoryCreateDTO;
import com.reger.application.dto.CategoryDTO;
import com.reger.application.dto.CategoryUpdateDTO;
import com.reger.application.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(categoryService.getAll(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) throws NotFoundException {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryUpdateDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(categoryDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteById(id);
    }
}