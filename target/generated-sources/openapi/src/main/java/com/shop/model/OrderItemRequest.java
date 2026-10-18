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
 * Chi tiết mặt hàng cần đặt
 */

@Schema(name = "OrderItemRequest", description = "Chi tiết mặt hàng cần đặt")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderItemRequest {

  private Long productId;

  private Integer quantity;

  public OrderItemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderItemRequest(Long productId, Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public OrderItemRequest productId(Long productId) {
    this.productId = productId;
    return this;
  }

  /**
   * ID sản phẩm
   * @return productId
   */
  @NotNull 
  @Schema(name = "productId", example = "101", description = "ID sản phẩm", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("productId")
  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public OrderItemRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Số lượng cần mua
   * minimum: 1
   * @return quantity
   */
  @NotNull @Min(1) 
  @Schema(name = "quantity", example = "2", description = "Số lượng cần mua", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemRequest orderItemRequest = (OrderItemRequest) o;
    return Objects.equals(this.productId, orderItemRequest.productId) &&
        Objects.equals(this.quantity, orderItemRequest.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderItemRequest {\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

