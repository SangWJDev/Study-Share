package com.studyshare.global.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.studyshare.global.common.response.ApiError;
import com.studyshare.global.common.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        var ec = e.getErrorCode();
        return ResponseEntity.status(ec.httpStatus())
                .body(ApiResponse.fail(ApiError.of(ec.code(), ec.defaultMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        ErrorCode errorCode = ApiErrorCode.VALIDATION_FAILED;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ApiError.of(errorCode.code(), message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e) {
        var ec = ApiErrorCode.INTERNAL_ERROR;
        log.error("서버 오류: {}", e.getMessage(), e);
        return ResponseEntity.status(ec.httpStatus())
                .body(ApiResponse.fail(ApiError.of(ec.code(), ec.defaultMessage())));
    }

}
