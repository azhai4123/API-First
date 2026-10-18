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
 * Dữ liệu thêm mới sản phẩm
 */

@Schema(name = "ProductCreateRequest", description = "Dữ liệu thêm mới sản phẩm")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class ProductCreateRequest {

  private String name;

  private String sku;

  private Double price;

  private Integer stockQuantity;

  private String category;

  private @Nullable Long categoryId;

  private @Nullable String description;

  private @Nullable URI imageUrl;

  public ProductCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProductCreateRequest(String name, String sku, Double price, Integer stockQuantity, String category) {
    this.name = name;
    this.sku = sku;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.category = category;
  }

  public ProductCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 2, max = 150) 
  @Schema(name = "name", example = "Bàn phím cơ không dây Keychron K2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductCreateRequest sku(String sku) {
    this.sku = sku;
    return this;
  }

  /**
   * Get sku
   * @return sku
   */
  @NotNull @Pattern(regexp = "^[A-Z0-9_-]{3,30}$") 
  @Schema(name = "sku", example = "KB-KEYCHRON-K2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sku")
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public ProductCreateRequest price(Double price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * minimum: 1000.0
   * @return price
   */
  @NotNull @DecimalMin("1000.0") 
  @Schema(name = "price", example = "1850000.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public ProductCreateRequest stockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  /**
   * Get stockQuantity
   * minimum: 0
   * @return stockQuantity
   */
  @NotNull @Min(0) 
  @Schema(name = "stockQuantity", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stockQuantity")
  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public ProductCreateRequest category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull @Size(min = 2, max = 50) 
  @Schema(name = "category", example = "Phụ kiện máy tính", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public ProductCreateRequest categoryId(Long categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  /**
   * ID danh mục bắt buộc
   * @return categoryId
   */
  
  @Schema(name = "categoryId", example = "1", description = "ID danh mục bắt buộc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoryId")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public ProductCreateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(max = 1000) 
  @Schema(name = "description", example = "Bàn phím cơ Bluetooth 75% Layout, switch Gateron Brown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductCreateRequest imageUrl(URI imageUrl) {
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
    ProductCreateRequest productCreateRequest = (ProductCreateRequest) o;
    return Objects.equals(this.name, productCreateRequest.name) &&
        Objects.equals(this.sku, productCreateRequest.sku) &&
        Objects.equals(this.price, productCreateRequest.price) &&
        Objects.equals(this.stockQuantity, productCreateRequest.stockQuantity) &&
        Objects.equals(this.category, productCreateRequest.category) &&
        Objects.equals(this.categoryId, productCreateRequest.categoryId) &&
        Objects.equals(this.description, productCreateRequest.description) &&
        Objects.equals(this.imageUrl, productCreateRequest.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, sku, price, stockQuantity, category, categoryId, description, imageUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    stockQuantity: ").append(toIndentedString(stockQuantity)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
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

