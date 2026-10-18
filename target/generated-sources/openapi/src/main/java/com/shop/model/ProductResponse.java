package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Dữ liệu chi tiết sản phẩm và tồn kho
 */

@Schema(name = "ProductResponse", description = "Dữ liệu chi tiết sản phẩm và tồn kho")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class ProductResponse {

  private Long id;

  private String name;

  private String sku;

  private Double price;

  private Integer stockQuantity;

  private String category;

  private @Nullable Long categoryId;

  private @Nullable String description;

  private @Nullable URI imageUrl;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE"),
    
    OUT_OF_STOCK("OUT_OF_STOCK");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public ProductResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProductResponse(Long id, String name, String sku, Double price, Integer stockQuantity, String category) {
    this.id = id;
    this.name = name;
    this.sku = sku;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.category = category;
  }

  public ProductResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID sản phẩm
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "101", description = "ID sản phẩm", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Tên thương mại sản phẩm
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Bàn phím cơ không dây Keychron K2", description = "Tên thương mại sản phẩm", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductResponse sku(String sku) {
    this.sku = sku;
    return this;
  }

  /**
   * Mã SKU quản lý tồn kho duy nhất
   * @return sku
   */
  @NotNull 
  @Schema(name = "sku", example = "KB-KEYCHRON-K2", description = "Mã SKU quản lý tồn kho duy nhất", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sku")
  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public ProductResponse price(Double price) {
    this.price = price;
    return this;
  }

  /**
   * Đơn giá niêm yết (VND)
   * @return price
   */
  @NotNull 
  @Schema(name = "price", example = "1850000.0", description = "Đơn giá niêm yết (VND)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public ProductResponse stockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  /**
   * Số lượng còn lại trong kho
   * @return stockQuantity
   */
  @NotNull 
  @Schema(name = "stockQuantity", example = "45", description = "Số lượng còn lại trong kho", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stockQuantity")
  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public ProductResponse category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Tên hiển thị danh mục
   * @return category
   */
  @NotNull 
  @Schema(name = "category", example = "Phụ kiện máy tính", description = "Tên hiển thị danh mục", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public ProductResponse categoryId(Long categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  /**
   * ID danh mục tham chiếu (Foreign Key)
   * @return categoryId
   */
  
  @Schema(name = "categoryId", example = "1", description = "ID danh mục tham chiếu (Foreign Key)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoryId")
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public ProductResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Mô tả thông số sản phẩm
   * @return description
   */
  
  @Schema(name = "description", example = "Bàn phím cơ Bluetooth 75% Layout, switch Gateron Brown", description = "Mô tả thông số sản phẩm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductResponse imageUrl(URI imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * Đường dẫn hình ảnh thực tế của sản phẩm
   * @return imageUrl
   */
  @Valid 
  @Schema(name = "imageUrl", example = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500", description = "Đường dẫn hình ảnh thực tế của sản phẩm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("imageUrl")
  public URI getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(URI imageUrl) {
    this.imageUrl = imageUrl;
  }

  public ProductResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", example = "ACTIVE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ProductResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Thời điểm thêm sản phẩm vào hệ thống (ISO-8601 UTC)
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", example = "2026-10-10T08:00Z", description = "Thời điểm thêm sản phẩm vào hệ thống (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ProductResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Thời điểm cập nhật sản phẩm gần nhất (ISO-8601 UTC)
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", example = "2026-10-15T12:00Z", description = "Thời điểm cập nhật sản phẩm gần nhất (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductResponse productResponse = (ProductResponse) o;
    return Objects.equals(this.id, productResponse.id) &&
        Objects.equals(this.name, productResponse.name) &&
        Objects.equals(this.sku, productResponse.sku) &&
        Objects.equals(this.price, productResponse.price) &&
        Objects.equals(this.stockQuantity, productResponse.stockQuantity) &&
        Objects.equals(this.category, productResponse.category) &&
        Objects.equals(this.categoryId, productResponse.categoryId) &&
        Objects.equals(this.description, productResponse.description) &&
        Objects.equals(this.imageUrl, productResponse.imageUrl) &&
        Objects.equals(this.status, productResponse.status) &&
        Objects.equals(this.createdAt, productResponse.createdAt) &&
        Objects.equals(this.updatedAt, productResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, sku, price, stockQuantity, category, categoryId, description, imageUrl, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    stockQuantity: ").append(toIndentedString(stockQuantity)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

