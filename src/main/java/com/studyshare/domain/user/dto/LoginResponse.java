package com.studyshare.domain.user.dto;

import com.studyshare.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private String email;

    private String nickname;

    public static LoginResponse from(String token, User user) {
        return LoginResponse.builder().accessToken(token).tokenType("Bearer").email(user.getEmail())
                .nickname(user.getNickname()).build();
    }

}
