package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Dữ liệu đăng ký khách hàng mới
 */

@Schema(name = "CustomerCreateRequest", description = "Dữ liệu đăng ký khách hàng mới")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class CustomerCreateRequest {

  private String fullName;

  private String email;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    CUSTOMER("CUSTOMER"),
    
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

  private RoleEnum role = RoleEnum.CUSTOMER;

  private @Nullable URI avatarUrl;

  private String phone;

  private String address;

  public CustomerCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CustomerCreateRequest(String fullName, String email, String phone, String address) {
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  public CustomerCreateRequest fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Họ và tên (tối thiểu 2 ký tự)
   * @return fullName
   */
  @NotNull @Size(min = 2, max = 100) 
  @Schema(name = "fullName", example = "Nguyen Van A", description = "Họ và tên (tối thiểu 2 ký tự)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fullName")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public CustomerCreateRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Email hợp lệ
   * @return email
   */
  @NotNull @Size(max = 100) @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "nguyenvana@example.com", description = "Email hợp lệ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CustomerCreateRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", example = "CUSTOMER", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public CustomerCreateRequest avatarUrl(URI avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  /**
   * Get avatarUrl
   * @return avatarUrl
   */
  @Valid 
  @Schema(name = "avatarUrl", example = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avatarUrl")
  public URI getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(URI avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public CustomerCreateRequest phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Số điện thoại từ 10 đến 11 số
   * @return phone
   */
  @NotNull @Pattern(regexp = "^[0-9]{10,11}$") 
  @Schema(name = "phone", example = "0901234567", description = "Số điện thoại từ 10 đến 11 số", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public CustomerCreateRequest address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Địa chỉ chi tiết
   * @return address
   */
  @NotNull @Size(min = 5, max = 255) 
  @Schema(name = "address", example = "72 Le Thanh Ton, Quan 1, TP.HCM", description = "Địa chỉ chi tiết", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerCreateRequest customerCreateRequest = (CustomerCreateRequest) o;
    return Objects.equals(this.fullName, customerCreateRequest.fullName) &&
        Objects.equals(this.email, customerCreateRequest.email) &&
        Objects.equals(this.role, customerCreateRequest.role) &&
        Objects.equals(this.avatarUrl, customerCreateRequest.avatarUrl) &&
        Objects.equals(this.phone, customerCreateRequest.phone) &&
        Objects.equals(this.address, customerCreateRequest.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, email, role, avatarUrl, phone, address);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerCreateRequest {\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    avatarUrl: ").append(toIndentedString(avatarUrl)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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

