package com.studyshare.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.studyshare.global.common.response.ApiError;
import com.studyshare.global.common.response.ApiResponse;

import org.springframework.http.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        var ec = e.getErrorCode();
        return ResponseEntity.status(ec.httpStatus()).body(ApiResponse.fail(ApiError.of(ec.code(), ec.defaultMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e) {
        var ec = ApiErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.httpStatus()).body(ApiResponse.fail(ApiError.of(ec.code(), ec.defaultMessage())));
    }
    
}
