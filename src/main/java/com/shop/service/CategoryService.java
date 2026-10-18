package com.shop.service;

import com.shop.entity.Category;
import com.shop.exception.ConflictException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.mapper.DtoMapper;
import com.shop.model.CategoryCreateRequest;
import com.shop.model.CategoryResponse;
import com.shop.model.CategoryUpdateRequest;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(DtoMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", "Không tìm thấy danh mục ID: " + id));
        return DtoMapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest req) {
        if (categoryRepository.existsByCode(req.getCode())) {
            throw new ConflictException("CATEGORY_CODE_EXISTS", "Mã danh mục '" + req.getCode() + "' đã tồn tại trên hệ thống");
        }
        if (categoryRepository.existsByName(req.getName())) {
            throw new ConflictException("CATEGORY_NAME_EXISTS", "Tên danh mục '" + req.getName() + "' đã tồn tại");
        }

        Category category = Category.builder()
                .code(req.getCode().toUpperCase().trim())
                .name(req.getName().trim())
                .description(req.getDescription())
                .status("ACTIVE")
                .build();

        Category saved = categoryRepository.save(category);
        return DtoMapper.toCategoryResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", "Không tìm thấy danh mục ID: " + id));

        if (req.getName() != null && !req.getName().isBlank()) {
            category.setName(req.getName().trim());
        }
        if (req.getDescription() != null) {
            category.setDescription(req.getDescription());
        }
        if (req.getStatus() != null) {
            category.setStatus(req.getStatus().getValue());
        }

        Category updated = categoryRepository.save(category);
        return DtoMapper.toCategoryResponse(updated);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", "Không tìm thấy danh mục ID: " + id));

        // Fail-Fast FF-02: Cấm xóa danh mục đang có sản phẩm liên kết
        long productCount = productRepository.countByCategoryId(id);
        if (productCount > 0) {
            throw new ConflictException("CATEGORY_IN_USE", "Không thể xóa danh mục đang liên kết với " + productCount + " sản phẩm");
        }

        categoryRepository.delete(category);
    }
}
