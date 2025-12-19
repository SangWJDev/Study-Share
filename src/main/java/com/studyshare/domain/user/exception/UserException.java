package com.studyshare.domain.user.exception;

import com.studyshare.global.exception.BusinessException;
import com.studyshare.global.exception.ErrorCode;

import lombok.Getter;

@Getter
public class UserException extends BusinessException{

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
    
}
