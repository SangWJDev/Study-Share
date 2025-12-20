package com.studyshare.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.studyshare.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 회원가입
    DUPLICATE_EMAIL("USER-001", HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME("USER-002", HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),

    // 로그인
    USER_NOT_FOUND("USER-003", HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD("USER-004", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 기타
    INVALID_REQUEST("USER-005", HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다.");

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
