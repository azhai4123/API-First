package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Dữ liệu thêm mới danh mục
 */

@Schema(name = "CategoryCreateRequest", description = "Dữ liệu thêm mới danh mục")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class CategoryCreateRequest {

  private String code;

  private String name;

  private @Nullable String description;

  public CategoryCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CategoryCreateRequest(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public CategoryCreateRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull @Pattern(regexp = "^[A-Z0-9_-]{3,30}$") 
  @Schema(name = "code", example = "MONITOR", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public CategoryCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 2, max = 100) 
  @Schema(name = "name", example = "Màn hình đồ họa", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CategoryCreateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(max = 500) 
  @Schema(name = "description", example = "Màn hình chuyên dụng cho thiết kế và gaming", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoryCreateRequest categoryCreateRequest = (CategoryCreateRequest) o;
    return Objects.equals(this.code, categoryCreateRequest.code) &&
        Objects.equals(this.name, categoryCreateRequest.name) &&
        Objects.equals(this.description, categoryCreateRequest.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryCreateRequest {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

