package com.studyshare.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyshare.domain.user.dto.SignupRequest;
import com.studyshare.domain.user.dto.SignupResponse;
import com.studyshare.domain.user.entity.User;
import com.studyshare.domain.user.exception.UserErrorCode;
import com.studyshare.domain.user.exception.UserException;
import com.studyshare.domain.user.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String email = request.getEmail();
        String nickname = request.getNickname();

        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        };

        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        String password = passwordEncoder.encode(request.getPassword());
        String bio = request.getBio();

        User user = userRepository
                .save(User.builder().email(email).password(password).nickname(request.getNickname())
                        .bio(bio).build());

        return SignupResponse.from(user);

    }

}
