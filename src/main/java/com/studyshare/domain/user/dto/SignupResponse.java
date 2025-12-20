package com.studyshare.domain.user.dto;

import java.time.LocalDateTime;

import com.studyshare.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SignupResponse {

    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    public static SignupResponse from(User user) {
        return SignupResponse.builder().id(user.getId()).email(user.getEmail()).nickname(user.getNickname())
                .createdAt(user.getCreatedAt()).build();
    }
}
