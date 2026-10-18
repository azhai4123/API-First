package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
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
 * Dữ liệu hồ sơ khách hàng trả về từ hệ thống
 */

@Schema(name = "CustomerResponse", description = "Dữ liệu hồ sơ khách hàng trả về từ hệ thống")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class CustomerResponse {

  private Long id;

  private String fullName;

  private String email;

  private String phone;

  private String address;

  /**
   * Trạng thái tài khoản
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE"),
    
    LOCKED("LOCKED");

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

  /**
   * Vai trò trong hệ thống
   */
  public enum RoleEnum {
    CUSTOMER("CUSTOMER"),
    
    STAFF("STAFF"),
    
    ADMIN("ADMIN");

    private String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private @Nullable URI avatarUrl;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public CustomerResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CustomerResponse(Long id, String fullName, String email, String phone, String address, StatusEnum status, OffsetDateTime createdAt) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.status = status;
    this.createdAt = createdAt;
  }

  public CustomerResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID định danh duy nhất của khách hàng
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "1", description = "ID định danh duy nhất của khách hàng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CustomerResponse fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Họ và tên đầy đủ
   * @return fullName
   */
  @NotNull 
  @Schema(name = "fullName", example = "Nguyen Van A", description = "Họ và tên đầy đủ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fullName")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public CustomerResponse email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Địa chỉ email định danh
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "nguyenvana@example.com", description = "Địa chỉ email định danh", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CustomerResponse phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Số điện thoại liên lạc
   * @return phone
   */
  @NotNull 
  @Schema(name = "phone", example = "0901234567", description = "Số điện thoại liên lạc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public CustomerResponse address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Địa chỉ giao hàng mặc định
   * @return address
   */
  @NotNull 
  @Schema(name = "address", example = "72 Le Thanh Ton, Quan 1, TP.HCM", description = "Địa chỉ giao hàng mặc định", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public CustomerResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Trạng thái tài khoản
   * @return status
   */
  @NotNull 
  @Schema(name = "status", example = "ACTIVE", description = "Trạng thái tài khoản", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public CustomerResponse role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Vai trò trong hệ thống
   * @return role
   */
  
  @Schema(name = "role", example = "CUSTOMER", description = "Vai trò trong hệ thống", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public CustomerResponse avatarUrl(URI avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  /**
   * Ảnh đại diện người dùng
   * @return avatarUrl
   */
  @Valid 
  @Schema(name = "avatarUrl", example = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", description = "Ảnh đại diện người dùng", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avatarUrl")
  public URI getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(URI avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public CustomerResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Thời điểm tạo tài khoản (ISO-8601 UTC)
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", example = "2026-10-14T08:30Z", description = "Thời điểm tạo tài khoản (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public CustomerResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Thời điểm cập nhật hồ sơ lần cuối (ISO-8601 UTC)
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", example = "2026-10-15T12:00Z", description = "Thời điểm cập nhật hồ sơ lần cuối (ISO-8601 UTC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    CustomerResponse customerResponse = (CustomerResponse) o;
    return Objects.equals(this.id, customerResponse.id) &&
        Objects.equals(this.fullName, customerResponse.fullName) &&
        Objects.equals(this.email, customerResponse.email) &&
        Objects.equals(this.phone, customerResponse.phone) &&
        Objects.equals(this.address, customerResponse.address) &&
        Objects.equals(this.status, customerResponse.status) &&
        Objects.equals(this.role, customerResponse.role) &&
        Objects.equals(this.avatarUrl, customerResponse.avatarUrl) &&
        Objects.equals(this.createdAt, customerResponse.createdAt) &&
        Objects.equals(this.updatedAt, customerResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, email, phone, address, status, role, avatarUrl, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    avatarUrl: ").append(toIndentedString(avatarUrl)).append("\n");
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

