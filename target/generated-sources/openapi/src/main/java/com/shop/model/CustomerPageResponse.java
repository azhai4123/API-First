package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.shop.model.CustomerResponse;
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
 * Trang danh sách khách hàng có phân trang
 */

@Schema(name = "CustomerPageResponse", description = "Trang danh sách khách hàng có phân trang")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class CustomerPageResponse {

  @Valid
  private List<@Valid CustomerResponse> content = new ArrayList<>();

  private PageMetadata page;

  public CustomerPageResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CustomerPageResponse(List<@Valid CustomerResponse> content, PageMetadata page) {
    this.content = content;
    this.page = page;
  }

  public CustomerPageResponse content(List<@Valid CustomerResponse> content) {
    this.content = content;
    return this;
  }

  public CustomerPageResponse addContentItem(CustomerResponse contentItem) {
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
  public List<@Valid CustomerResponse> getContent() {
    return content;
  }

  public void setContent(List<@Valid CustomerResponse> content) {
    this.content = content;
  }

  public CustomerPageResponse page(PageMetadata page) {
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
    CustomerPageResponse customerPageResponse = (CustomerPageResponse) o;
    return Objects.equals(this.content, customerPageResponse.content) &&
        Objects.equals(this.page, customerPageResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerPageResponse {\n");
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

