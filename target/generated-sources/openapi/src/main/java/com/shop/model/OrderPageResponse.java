package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.shop.model.OrderResponse;
import com.shop.model.PageMetadata;
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
 * Trang danh sách đơn hàng
 */

@Schema(name = "OrderPageResponse", description = "Trang danh sách đơn hàng")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderPageResponse {

  @Valid
  private List<@Valid OrderResponse> content = new ArrayList<>();

  private PageMetadata page;

  public OrderPageResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderPageResponse(List<@Valid OrderResponse> content, PageMetadata page) {
    this.content = content;
    this.page = page;
  }

  public OrderPageResponse content(List<@Valid OrderResponse> content) {
    this.content = content;
    return this;
  }

  public OrderPageResponse addContentItem(OrderResponse contentItem) {
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
  public List<@Valid OrderResponse> getContent() {
    return content;
  }

  public void setContent(List<@Valid OrderResponse> content) {
    this.content = content;
  }

  public OrderPageResponse page(PageMetadata page) {
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
    OrderPageResponse orderPageResponse = (OrderPageResponse) o;
    return Objects.equals(this.content, orderPageResponse.content) &&
        Objects.equals(this.page, orderPageResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderPageResponse {\n");
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

