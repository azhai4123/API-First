package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.shop.model.PageMetadata;
import com.shop.model.ProductResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Trang danh sách sản phẩm
 */

@Schema(name = "ProductPageResponse", description = "Trang danh sách sản phẩm")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class ProductPageResponse {

  @Valid
  private List<@Valid ProductResponse> content = new ArrayList<>();

  private PageMetadata page;

  public ProductPageResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProductPageResponse(List<@Valid ProductResponse> content, PageMetadata page) {
    this.content = content;
    this.page = page;
  }

  public ProductPageResponse content(List<@Valid ProductResponse> content) {
    this.content = content;
    return this;
  }

  public ProductPageResponse addContentItem(ProductResponse contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

  /**
   * Get content
   * @return content
   */
  @NotNull @Valid 
  @Schema(name = "content", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("content")
  public List<@Valid ProductResponse> getContent() {
    return content;
  }

  public void setContent(List<@Valid ProductResponse> content) {
    this.content = content;
  }

  public ProductPageResponse page(PageMetadata page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  @NotNull @Valid 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("page")
  public PageMetadata getPage() {
    return page;
  }

  public void setPage(PageMetadata page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductPageResponse productPageResponse = (ProductPageResponse) o;
    return Objects.equals(this.content, productPageResponse.content) &&
        Objects.equals(this.page, productPageResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductPageResponse {\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

