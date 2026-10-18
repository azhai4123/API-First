package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Dữ liệu cập nhật một phần sản phẩm (giá, tồn kho, mô tả, danh mục)
 */

@Schema(name = "ProductPatchRequest", description = "Dữ liệu cập nhật một phần sản phẩm (giá, tồn kho, mô tả, danh mục)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class ProductPatchRequest {

  private @Nullable String name;

  private @Nullable Long categoryId;

  private @Nullable String category;

  private @Nullable Double price;

  private @Nullable Integer stockQuantity;

  private @Nullable String description;

  private @Nullable URI imageUrl;

  public ProductPatchRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @Size(min = 2, max = 150) 
  @Schema(name = "name", example = "Bàn phím cơ không dây Keychron K2 V2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductPatchRequest categoryId(Long categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  /**
   * Get categoryId
   * @return categoryId
   */
  
  @Schema(name = "categoryId", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoryId")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public ProductPatchRequest category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", example = "Phụ kiện", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public ProductPatchRequest price(Double price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * minimum: 1000.0
   * @return price
   */
  @DecimalMin("1000.0") 
  @Schema(name = "price", example = "1950000.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public ProductPatchRequest stockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  /**
   * Get stockQuantity
   * minimum: 0
   * @return stockQuantity
   */
  @Min(0) 
  @Schema(name = "stockQuantity", example = "40", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stockQuantity")
  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public ProductPatchRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(max = 1000) 
  @Schema(name = "description", example = "Phiên bản nâng cấp Led RGB Hot-swap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductPatchRequest imageUrl(URI imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * Get imageUrl
   * @return imageUrl
   */
  @Valid 
  @Schema(name = "imageUrl", example = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("imageUrl")
  public URI getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(URI imageUrl) {
    this.imageUrl = imageUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductPatchRequest productPatchRequest = (ProductPatchRequest) o;
    return Objects.equals(this.name, productPatchRequest.name) &&
        Objects.equals(this.categoryId, productPatchRequest.categoryId) &&
        Objects.equals(this.category, productPatchRequest.category) &&
        Objects.equals(this.price, productPatchRequest.price) &&
        Objects.equals(this.stockQuantity, productPatchRequest.stockQuantity) &&
        Objects.equals(this.description, productPatchRequest.description) &&
        Objects.equals(this.imageUrl, productPatchRequest.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, categoryId, category, price, stockQuantity, description, imageUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductPatchRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    stockQuantity: ").append(toIndentedString(stockQuantity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

