package com.shop.exception;

import com.shop.model.ErrorDetail;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UnprocessableEntityException extends ApiException {

    public UnprocessableEntityException(String code, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, code, message);
    }

    public UnprocessableEntityException(String code, String message, List<ErrorDetail> details) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, code, message, details);
    }
}
