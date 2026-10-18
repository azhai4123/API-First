package com.shop.controller;

import com.shop.api.CategoriesApi;
import com.shop.model.CategoryCreateRequest;
import com.shop.model.CategoryResponse;
import com.shop.model.CategoryUpdateRequest;
import com.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriesController implements CategoriesApi {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<CategoryResponse> createCategory(CategoryCreateRequest categoryCreateRequest) {
        CategoryResponse created = categoryService.createCategory(categoryCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + created.getId())).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Override
    public ResponseEntity<CategoryResponse> getCategoryById(Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Override
    public ResponseEntity<CategoryResponse> updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryUpdateRequest));
    }
}
