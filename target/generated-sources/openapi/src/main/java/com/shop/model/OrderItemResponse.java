package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Chi tiết mặt hàng trong đơn hàng kèm giá bảo lưu lịch sử
 */

@Schema(name = "OrderItemResponse", description = "Chi tiết mặt hàng trong đơn hàng kèm giá bảo lưu lịch sử")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderItemResponse {

  private Long id;

  private Long productId;

  private String productName;

  private Integer quantity;

  private Double unitPrice;

  private Double subtotal;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public OrderItemResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderItemResponse(Long id, Long productId, String productName, Integer quantity, Double unitPrice, Double subtotal) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.subtotal = subtotal;
  }

  public OrderItemResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "9001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderItemResponse productId(Long productId) {
    this.productId = productId;
    return this;
  }

  /**
   * Get productId
   * @return productId
   */
  @NotNull 
  @Schema(name = "productId", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("productId")
  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public OrderItemResponse productName(String productName) {
    this.productName = productName;
    return this;
  }

  /**
   * Get productName
   * @return productName
   */
  @NotNull 
  @Schema(name = "productName", example = "Bàn phím cơ không dây Keychron K2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("productName")
  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public OrderItemResponse quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public OrderItemResponse unitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  /**
   * Đơn giá cố định tại thời điểm tạo đơn
   * @return unitPrice
   */
  @NotNull 
  @Schema(name = "unitPrice", example = "1850000.0", description = "Đơn giá cố định tại thời điểm tạo đơn", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("unitPrice")
  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public OrderItemResponse subtotal(Double subtotal) {
    this.subtotal = subtotal;
    return this;
  }

  /**
   * Thành tiền mục hàng
   * @return subtotal
   */
  @NotNull 
  @Schema(name = "subtotal", example = "3700000.0", description = "Thành tiền mục hàng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subtotal")
  public Double getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(Double subtotal) {
    this.subtotal = subtotal;
  }

  public OrderItemResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Thời điểm tạo dòng mục hàng (ISO-8601 UTC)
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", example = "2026-10-14T09:15Z", description = "Thời điểm tạo dòng mục hàng (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemResponse orderItemResponse = (OrderItemResponse) o;
    return Objects.equals(this.id, orderItemResponse.id) &&
        Objects.equals(this.productId, orderItemResponse.productId) &&
        Objects.equals(this.productName, orderItemResponse.productName) &&
        Objects.equals(this.quantity, orderItemResponse.quantity) &&
        Objects.equals(this.unitPrice, orderItemResponse.unitPrice) &&
        Objects.equals(this.subtotal, orderItemResponse.subtotal) &&
        Objects.equals(this.createdAt, orderItemResponse.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, productName, quantity, unitPrice, subtotal, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderItemResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    productName: ").append(toIndentedString(productName)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    unitPrice: ").append(toIndentedString(unitPrice)).append("\n");
    sb.append("    subtotal: ").append(toIndentedString(subtotal)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

