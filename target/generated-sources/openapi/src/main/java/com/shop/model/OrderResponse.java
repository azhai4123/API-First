package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shop.model.OrderItemResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Chi tiết đơn hàng hoàn chỉnh
 */

@Schema(name = "OrderResponse", description = "Chi tiết đơn hàng hoàn chỉnh")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderResponse {

  private Long id;

  private Long customerId;

  private Double totalAmount;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    PAID("PAID"),
    
    SHIPPED("SHIPPED"),
    
    CANCELLED("CANCELLED"),
    
    CONFIRMED("CONFIRMED"),
    
    DELIVERED("DELIVERED");

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

  private String shippingAddress;

  @Valid
  private List<@Valid OrderItemResponse> items = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public OrderResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderResponse(Long id, Long customerId, Double totalAmount, StatusEnum status, String shippingAddress, List<@Valid OrderItemResponse> items, OffsetDateTime createdAt) {
    this.id = id;
    this.customerId = customerId;
    this.totalAmount = totalAmount;
    this.status = status;
    this.shippingAddress = shippingAddress;
    this.items = items;
    this.createdAt = createdAt;
  }

  public OrderResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "5001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderResponse customerId(Long customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
   */
  @NotNull 
  @Schema(name = "customerId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerId")
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public OrderResponse totalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  /**
   * Tổng giá trị đơn hàng
   * @return totalAmount
   */
  @NotNull 
  @Schema(name = "totalAmount", example = "3700000.0", description = "Tổng giá trị đơn hàng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalAmount")
  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public OrderResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", example = "PENDING", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public OrderResponse shippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  /**
   * Get shippingAddress
   * @return shippingAddress
   */
  @NotNull 
  @Schema(name = "shippingAddress", example = "72 Le Thanh Ton, Quan 1, TP.HCM", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shippingAddress")
  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public OrderResponse items(List<@Valid OrderItemResponse> items) {
    this.items = items;
    return this;
  }

  public OrderResponse addItemsItem(OrderItemResponse itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid OrderItemResponse> getItems() {
    return items;
  }

  public void setItems(List<@Valid OrderItemResponse> items) {
    this.items = items;
  }

  public OrderResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", example = "2026-10-14T09:15Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OrderResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Thời điểm cập nhật đơn hàng lần cuối (ISO-8601 UTC)
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", example = "2026-10-15T12:00Z", description = "Thời điểm cập nhật đơn hàng lần cuối (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    OrderResponse orderResponse = (OrderResponse) o;
    return Objects.equals(this.id, orderResponse.id) &&
        Objects.equals(this.customerId, orderResponse.customerId) &&
        Objects.equals(this.totalAmount, orderResponse.totalAmount) &&
        Objects.equals(this.status, orderResponse.status) &&
        Objects.equals(this.shippingAddress, orderResponse.shippingAddress) &&
        Objects.equals(this.items, orderResponse.items) &&
        Objects.equals(this.createdAt, orderResponse.createdAt) &&
        Objects.equals(this.updatedAt, orderResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, totalAmount, status, shippingAddress, items, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    totalAmount: ").append(toIndentedString(totalAmount)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    shippingAddress: ").append(toIndentedString(shippingAddress)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

