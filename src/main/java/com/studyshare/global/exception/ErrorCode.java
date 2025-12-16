package com.studyshare.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String code();

    HttpStatus httpStatus();

    String defaultMessage();

}
