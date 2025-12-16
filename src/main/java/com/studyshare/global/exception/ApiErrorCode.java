package com.studyshare.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiErrorCode implements ErrorCode {

    // ===== 공통 =====
    INVALID_REQUEST("COMMON-001", HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다."),
    VALIDATION_FAILED("COMMON-002", HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다."),
    UNSUPPORTED_MEDIA_TYPE("COMMON-003", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 Content-Type 입니다."),

    // ===== 인증/인가 =====
    UNAUTHORIZED("AUTH-001", HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN("AUTH-002", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    TOKEN_EXPIRED("AUTH-003", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // ===== 리소스 =====
    RESOURCE_NOT_FOUND("RES-001", HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    DUPLICATE_RESOURCE("RES-002", HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),

    // ===== 제한/쿼터 =====
    RATE_LIMITED("LIMIT-001", HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다."),

    // ===== 서버 =====
    INTERNAL_ERROR("SYS-001", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    UPSTREAM_ERROR("SYS-002", HttpStatus.BAD_GATEWAY, "외부 시스템 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE("SYS-003", HttpStatus.SERVICE_UNAVAILABLE, "일시적으로 사용할 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    @Override
    public String code() {
        return code;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

}
