package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.shop.model.OrderItemRequest;
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
 * Yêu cầu đặt hàng mới
 */

@Schema(name = "OrderCreateRequest", description = "Yêu cầu đặt hàng mới")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderCreateRequest {

  private Long customerId;

  private String shippingAddress;

  @Valid
  private List<@Valid OrderItemRequest> items = new ArrayList<>();

  public OrderCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderCreateRequest(Long customerId, String shippingAddress, List<@Valid OrderItemRequest> items) {
    this.customerId = customerId;
    this.shippingAddress = shippingAddress;
    this.items = items;
  }

  public OrderCreateRequest customerId(Long customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * ID khách hàng đặt mua
   * @return customerId
   */
  @NotNull 
  @Schema(name = "customerId", example = "1", description = "ID khách hàng đặt mua", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerId")
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public OrderCreateRequest shippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  /**
   * Địa chỉ nhận hàng
   * @return shippingAddress
   */
  @NotNull @Size(min = 5, max = 255) 
  @Schema(name = "shippingAddress", example = "72 Le Thanh Ton, Quan 1, TP.HCM", description = "Địa chỉ nhận hàng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shippingAddress")
  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public OrderCreateRequest items(List<@Valid OrderItemRequest> items) {
    this.items = items;
    return this;
  }

  public OrderCreateRequest addItemsItem(OrderItemRequest itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Danh sách sản phẩm trong giỏ hàng
   * @return items
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "items", description = "Danh sách sản phẩm trong giỏ hàng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid OrderItemRequest> getItems() {
    return items;
  }

  public void setItems(List<@Valid OrderItemRequest> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderCreateRequest orderCreateRequest = (OrderCreateRequest) o;
    return Objects.equals(this.customerId, orderCreateRequest.customerId) &&
        Objects.equals(this.shippingAddress, orderCreateRequest.shippingAddress) &&
        Objects.equals(this.items, orderCreateRequest.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, shippingAddress, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderCreateRequest {\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    shippingAddress: ").append(toIndentedString(shippingAddress)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

