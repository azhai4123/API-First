package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Cập nhật trạng thái xử lý đơn hàng
 */

@Schema(name = "OrderStatusUpdateRequest", description = "Cập nhật trạng thái xử lý đơn hàng")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class OrderStatusUpdateRequest {

  /**
   * Trạng thái mới của đơn hàng (Theo chuẩn: PENDING -> PAID -> SHIPPED hoặc CANCELLED)
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

  public OrderStatusUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderStatusUpdateRequest(StatusEnum status) {
    this.status = status;
  }

  public OrderStatusUpdateRequest status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Trạng thái mới của đơn hàng (Theo chuẩn: PENDING -> PAID -> SHIPPED hoặc CANCELLED)
   * @return status
   */
  @NotNull 
  @Schema(name = "status", example = "PAID", description = "Trạng thái mới của đơn hàng (Theo chuẩn: PENDING -> PAID -> SHIPPED hoặc CANCELLED)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderStatusUpdateRequest orderStatusUpdateRequest = (OrderStatusUpdateRequest) o;
    return Objects.equals(this.status, orderStatusUpdateRequest.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderStatusUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

