package com.studyshare.global.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyshare.global.common.response.ApiError;
import com.studyshare.global.common.response.ApiResponse;
import com.studyshare.global.exception.ApiErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiResponse<Void> errorResponse = ApiResponse.fail(ApiError.of(ApiErrorCode.UNAUTHORIZED));
        String json = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }

}
