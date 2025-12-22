package com.studyshare.domain.group.exception;

import org.springframework.http.HttpStatus;

import com.studyshare.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements ErrorCode {

    // 그룹 관련
    GROUP_NOT_FOUND("GROUP-001", HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    INVITE_CODE_DUPLICATED("GROUP-002", HttpStatus.CONFLICT, "초대 코드가 중복되었습니다."),
    
    // 멤버 관련
    GROUP_MEMBER_FULL("GROUP-003", HttpStatus.BAD_REQUEST, "그룹 인원이 가득 찼습니다."),
    ALREADY_GROUP_MEMBER("GROUP-004", HttpStatus.CONFLICT, "이미 그룹에 가입되어 있습니다."),
    NOT_GROUP_MEMBER("GROUP-005", HttpStatus.FORBIDDEN, "그룹 멤버가 아닙니다."),

    // 권한 관련
    NOT_GROUP_LEADER("GROUP-006", HttpStatus.FORBIDDEN, "그룹 리더만 가능한 작업입니다."),
    CANNOT_LEAVE_AS_LEADER("GROUP-007", HttpStatus.BAD_REQUEST, "리더는 그룹을 탈퇴할 수 없습니다."),

    // 초대 관련
    INVALID_INVITE_CODE("GROUP-008", HttpStatus.BAD_REQUEST, "유효하지 않은 초대 코드입니다.");

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
