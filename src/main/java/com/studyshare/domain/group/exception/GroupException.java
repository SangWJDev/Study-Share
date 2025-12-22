package com.studyshare.domain.group.exception;

import com.studyshare.global.exception.BusinessException;
import com.studyshare.global.exception.ErrorCode;

public class GroupException extends BusinessException {


    public GroupException(ErrorCode errorCode) {
        super(errorCode);
        
    }
    
}
