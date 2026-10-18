package com.shop.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.shop.model.ErrorDetail;
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
 * Cấu trúc phản hồi lỗi chuẩn mực toàn hệ thống
 */

@Schema(name = "ErrorResponse", description = "Cấu trúc phản hồi lỗi chuẩn mực toàn hệ thống")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-10-16T20:55:41.108634500+07:00[Asia/Saigon]", comments = "Generator version: 7.11.0")
public class ErrorResponse {

  private String code;

  private String message;

  @Valid
  private List<@Valid ErrorDetail> details = new ArrayList<>();

  public ErrorResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public ErrorResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Mã định danh lỗi máy đọc được (Machine-readable)
   * @return code
   */
  @NotNull 
  @Schema(name = "code", example = "VALIDATION_FAILED", description = "Mã định danh lỗi máy đọc được (Machine-readable)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Thông điệp giải thích lỗi dễ hiểu cho người dùng
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Dữ liệu yêu cầu không vượt qua kiểm tra ràng buộc", description = "Thông điệp giải thích lỗi dễ hiểu cho người dùng", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ErrorResponse details(List<@Valid ErrorDetail> details) {
    this.details = details;
    return this;
  }

  public ErrorResponse addDetailsItem(ErrorDetail detailsItem) {
    if (this.details == null) {
      this.details = new ArrayList<>();
    }
    this.details.add(detailsItem);
    return this;
  }

  /**
   * Danh sách các lỗi chi tiết (nếu có)
   * @return details
   */
  @Valid 
  @Schema(name = "details", description = "Danh sách các lỗi chi tiết (nếu có)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public List<@Valid ErrorDetail> getDetails() {
    return details;
  }

  public void setDetails(List<@Valid ErrorDetail> details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this.code, errorResponse.code) &&
        Objects.equals(this.message, errorResponse.message) &&
        Objects.equals(this.details, errorResponse.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

