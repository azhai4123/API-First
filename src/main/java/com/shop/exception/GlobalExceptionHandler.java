package com.shop.exception;

import com.shop.model.ErrorDetail;
import com.shop.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setDetails(ex.getDetails());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            ErrorDetail d = new ErrorDetail();
            d.setField(fe.getField());
            d.setIssue(fe.getDefaultMessage());
            details.add(d);
        }

        ErrorResponse response = new ErrorResponse();
        response.setCode("VALIDATION_FAILED");
        response.setMessage("Dữ liệu gửi lên không đúng định dạng quy định");
        response.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("UNAUTHORIZED");
        response.setMessage("Email hoặc mật khẩu không chính xác");
        response.setDetails(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("ACCESS_DENIED");
        response.setMessage("Bạn không có quyền thực hiện thao tác này");
        response.setDetails(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = new ErrorResponse();
        response.setCode("INTERNAL_SERVER_ERROR");
        response.setMessage("Lỗi máy chủ nội bộ: " + ex.getMessage());
        response.setDetails(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
