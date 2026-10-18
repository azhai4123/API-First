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
 * Siêu dữ liệu phân trang chuẩn RESTful
 */

@Schema(name = "PageMetadata", description = "Siêu dữ liệu phân trang chuẩn RESTful")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class PageMetadata {

  private Integer number;

  private Integer size;

  private Long totalElements;

  private Integer totalPages;

  private Boolean last;

  public PageMetadata() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PageMetadata(Integer number, Integer size, Long totalElements, Integer totalPages, Boolean last) {
    this.number = number;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.last = last;
  }

  public PageMetadata number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Chỉ số trang hiện tại (bắt đầu từ 0)
   * @return number
   */
  @NotNull 
  @Schema(name = "number", example = "0", description = "Chỉ số trang hiện tại (bắt đầu từ 0)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("number")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PageMetadata size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Kích thước số bản ghi trên một trang
   * @return size
   */
  @NotNull 
  @Schema(name = "size", example = "10", description = "Kích thước số bản ghi trên một trang", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public PageMetadata totalElements(Long totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Tổng số lượng bản ghi thỏa mãn điều kiện
   * @return totalElements
   */
  @NotNull 
  @Schema(name = "totalElements", example = "42", description = "Tổng số lượng bản ghi thỏa mãn điều kiện", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalElements")
  public Long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  public PageMetadata totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Tổng số trang có thể phân bổ
   * @return totalPages
   */
  @NotNull 
  @Schema(name = "totalPages", example = "5", description = "Tổng số trang có thể phân bổ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public PageMetadata last(Boolean last) {
    this.last = last;
    return this;
  }

  /**
   * Cờ đánh dấu có phải trang cuối cùng hay không
   * @return last
   */
  @NotNull 
  @Schema(name = "last", example = "false", description = "Cờ đánh dấu có phải trang cuối cùng hay không", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("last")
  public Boolean getLast() {
    return last;
  }

  public void setLast(Boolean last) {
    this.last = last;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageMetadata pageMetadata = (PageMetadata) o;
    return Objects.equals(this.number, pageMetadata.number) &&
        Objects.equals(this.size, pageMetadata.size) &&
        Objects.equals(this.totalElements, pageMetadata.totalElements) &&
        Objects.equals(this.totalPages, pageMetadata.totalPages) &&
        Objects.equals(this.last, pageMetadata.last);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, size, totalElements, totalPages, last);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageMetadata {\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    last: ").append(toIndentedString(last)).append("\n");
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

