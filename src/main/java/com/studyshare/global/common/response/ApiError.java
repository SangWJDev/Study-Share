package com.studyshare.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {

    private String code;

    private String message;

    public static ApiError of(String code, String message) {
        return new ApiError(code, message);
    }

}