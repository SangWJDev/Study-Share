package com.studyshare.global.common.response;

import com.studyshare.global.exception.ApiError;
import com.studyshare.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;

    private ApiError error;

    private T data;

    public static <T> ApiResponse<T> ok(T data) {

        return new ApiResponse<>(true, null, data);

    }
    
    public static ApiResponse<Void> ok() {

        return new ApiResponse<Void>(true, null, null);

    }

    public static ApiResponse<Void> fail(ApiError error) {
        
        return new ApiResponse<Void>(false, error, null);
    }
 
}
