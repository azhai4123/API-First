package com.shop.controller;

import com.shop.api.ProductsApi;
import com.shop.model.ProductCreateRequest;
import com.shop.model.ProductPageResponse;
import com.shop.model.ProductPatchRequest;
import com.shop.model.ProductResponse;
import com.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ProductsController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductCreateRequest productCreateRequest) {
        ProductResponse created = productService.createProduct(productCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/products/" + created.getId())).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductPageResponse> getProducts(Integer page, Integer size, String sort, String category) {
        return ResponseEntity.ok(productService.getProducts(page, size, category, null, null, sort));
    }

    @Override
    public ResponseEntity<ProductResponse> patchProduct(Long id, ProductPatchRequest productPatchRequest) {
        return ResponseEntity.ok(productService.patchProduct(id, productPatchRequest));
    }
}
