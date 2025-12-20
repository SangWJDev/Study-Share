package com.studyshare.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.studyshare.domain.user.dto.LoginRequest;
import com.studyshare.domain.user.dto.LoginResponse;
import com.studyshare.domain.user.dto.SignupRequest;
import com.studyshare.domain.user.dto.SignupResponse;
import com.studyshare.domain.user.entity.User;
import com.studyshare.domain.user.exception.UserErrorCode;
import com.studyshare.domain.user.exception.UserException;
import com.studyshare.domain.user.repository.UserRepository;
import com.studyshare.global.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest(
                "test@example.com",
                "password123",
                "테스터",
                "안녕하세요");

        loginRequest = new LoginRequest(
                "test@example.com",
                "password123");

        user = User.builder()
                .email("test@example.com")
                .password("encrypted_password")
                .nickname("테스터")
                .bio("안녕하세요")
                .build();
    }

    @Test
    @DisplayName("정상 회원가입 성공")
    void signup_Success() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(signupRequest.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encrypted_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        SignupResponse response = userService.signup(signupRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("테스터");

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).existsByNickname(signupRequest.getNickname());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 시 예외 발생")
    void signup_DuplicateEmail_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DUPLICATE_EMAIL);

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).existsByNickname(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("닉네임 중복 시 예외 발생")
    void signup_DuplicateNickname_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(signupRequest.getNickname())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.DUPLICATE_NICKNAME);

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).existsByNickname(signupRequest.getNickname());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("정상 로그인 성공 및 토큰 반환")
    void login_Success() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateAccessToken(loginRequest.getEmail())).thenReturn("jwt.token.example");

        // When
        LoginResponse response = userService.login(loginRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt.token.example");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("테스터");

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil).generateAccessToken(loginRequest.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 시 예외 발생")
    void login_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_NOT_FOUND);

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString());
    }

    @Test
    @DisplayName("비밀번호 불일치 시 예외 발생")
    void login_InvalidPassword_ThrowsException() {
        // Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(UserException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.INVALID_PASSWORD);

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
        verify(jwtUtil, never()).generateAccessToken(anyString());
    }

}
