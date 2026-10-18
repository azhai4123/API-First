package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Chi tiết danh mục sản phẩm
 */

@Schema(name = "CategoryResponse", description = "Chi tiết danh mục sản phẩm")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class CategoryResponse {

  private Long id;

  private String code;

  private String name;

  private @Nullable String description;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public CategoryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CategoryResponse(Long id, String code, String name, StatusEnum status) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.status = status;
  }

  public CategoryResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID danh mục
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "1", description = "ID danh mục", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CategoryResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Mã định danh phân loại duy nhất
   * @return code
   */
  @NotNull 
  @Schema(name = "code", example = "ACCESSORIES", description = "Mã định danh phân loại duy nhất", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public CategoryResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Tên hiển thị danh mục
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Phụ kiện máy tính", description = "Tên hiển thị danh mục", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CategoryResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Mô tả thông tin danh mục
   * @return description
   */
  
  @Schema(name = "description", example = "Bàn phím, chuột, tai nghe và giá treo màn hình", description = "Mô tả thông tin danh mục", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CategoryResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public CategoryResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Thời điểm khởi tạo danh mục (ISO-8601 UTC)
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", example = "2026-10-10T08:00Z", description = "Thời điểm khởi tạo danh mục (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public CategoryResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Thời điểm cập nhật danh mục lần cuối (ISO-8601 UTC)
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", example = "2026-10-15T12:00Z", description = "Thời điểm cập nhật danh mục lần cuối (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    CategoryResponse categoryResponse = (CategoryResponse) o;
    return Objects.equals(this.id, categoryResponse.id) &&
        Objects.equals(this.code, categoryResponse.code) &&
        Objects.equals(this.name, categoryResponse.name) &&
        Objects.equals(this.description, categoryResponse.description) &&
        Objects.equals(this.status, categoryResponse.status) &&
        Objects.equals(this.createdAt, categoryResponse.createdAt) &&
        Objects.equals(this.updatedAt, categoryResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, description, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

