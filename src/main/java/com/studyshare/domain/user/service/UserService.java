package com.studyshare.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyshare.domain.user.dto.LoginRequest;
import com.studyshare.domain.user.dto.LoginResponse;
import com.studyshare.domain.user.dto.SignupRequest;
import com.studyshare.domain.user.dto.SignupResponse;
import com.studyshare.domain.user.entity.User;
import com.studyshare.domain.user.exception.UserErrorCode;
import com.studyshare.domain.user.exception.UserException;
import com.studyshare.domain.user.repository.UserRepository;
import com.studyshare.global.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String email = request.getEmail();
        String nickname = request.getNickname();

        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

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

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        String token = jwtUtil.generateAccessToken(email);

        return LoginResponse.from(token, user);

    }

}
