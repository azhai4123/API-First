package com.shop.service;

import com.shop.entity.Category;
import com.shop.entity.Product;
import com.shop.exception.ConflictException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.mapper.DtoMapper;
import com.shop.model.*;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductPageResponse getProducts(Integer page, Integer size, String category, String stock, String search, String sort) {
        int pageNumber = (page != null && page > 0) ? page - 1 : 0;
        int pageSize = (size != null && size > 0) ? size : 10;

        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort != null) {
            if ("price,asc".equalsIgnoreCase(sort)) sorting = Sort.by(Sort.Direction.ASC, "price");
            else if ("price,desc".equalsIgnoreCase(sort)) sorting = Sort.by(Sort.Direction.DESC, "price");
            else if ("name,asc".equalsIgnoreCase(sort)) sorting = Sort.by(Sort.Direction.ASC, "name");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) {
                predicates.add(cb.like(cb.lower(root.get("category").get("name")), "%" + category.toLowerCase().trim() + "%"));
            }
            if ("in_stock".equalsIgnoreCase(stock)) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            } else if ("out_of_stock".equalsIgnoreCase(stock)) {
                predicates.add(cb.equal(root.get("stockQuantity"), 0));
            }
            if (search != null && !search.isBlank()) {
                String term = "%" + search.toLowerCase().trim() + "%";
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), term);
                Predicate skuMatch = cb.like(cb.lower(root.get("sku")), term);
                predicates.add(cb.or(nameMatch, skuMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        ProductPageResponse resp = new ProductPageResponse();
        resp.setContent(productPage.getContent().stream().map(DtoMapper::toProductResponse).collect(Collectors.toList()));

        PageMetadata pageMeta = new PageMetadata();
        pageMeta.setNumber(productPage.getNumber() + 1);
        pageMeta.setSize(productPage.getSize());
        pageMeta.setTotalElements(productPage.getTotalElements());
        pageMeta.setTotalPages(productPage.getTotalPages());
        pageMeta.setLast(productPage.isLast());
        resp.setPage(pageMeta);

        return resp;
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm ID: " + id));
        return DtoMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest req) {
        // Fail-Fast FF-05: Kiểm tra SKU duy nhất
        if (productRepository.existsBySku(req.getSku())) {
            throw new ConflictException("SKU_ALREADY_EXISTS", "Mã SKU '" + req.getSku() + "' đã tồn tại");
        }

        // Fail-Fast FF-02: Ràng buộc khóa ngoại CategoryId
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", "Danh mục ID " + req.getCategoryId() + " không tồn tại"));

        Product product = Product.builder()
                .category(category)
                .name(req.getName().trim())
                .sku(req.getSku().trim().toUpperCase())
                .price(BigDecimal.valueOf(req.getPrice()))
                .stockQuantity(req.getStockQuantity())
                .imageUrl(req.getImageUrl() != null ? req.getImageUrl().toString() : "")
                .description(req.getDescription())
                .status(req.getStockQuantity() > 0 ? "ACTIVE" : "OUT_OF_STOCK")
                .build();

        Product saved = productRepository.save(product);
        return DtoMapper.toProductResponse(saved);
    }

    @Transactional
    public ProductResponse patchProduct(Long id, ProductPatchRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm ID: " + id));

        if (req.getName() != null && !req.getName().isBlank()) product.setName(req.getName().trim());
        if (req.getPrice() != null) product.setPrice(BigDecimal.valueOf(req.getPrice()));
        if (req.getStockQuantity() != null) {
            product.setStockQuantity(req.getStockQuantity());
            product.setStatus(req.getStockQuantity() > 0 ? "ACTIVE" : "OUT_OF_STOCK");
        }
        if (req.getImageUrl() != null) product.setImageUrl(req.getImageUrl().toString());
        if (req.getDescription() != null) product.setDescription(req.getDescription());

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", "Danh mục ID " + req.getCategoryId() + " không tồn tại"));
            product.setCategory(cat);
        }

        Product updated = productRepository.save(product);
        return DtoMapper.toProductResponse(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm ID: " + id));
        productRepository.delete(product);
    }
}
