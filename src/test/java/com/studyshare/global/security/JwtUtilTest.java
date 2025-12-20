package com.studyshare.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testEmail;
    private SecretKey testSecretKey;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        testEmail = "test@example.com";

        // Reflection을 사용하여 private 필드 설정
        String secretKeyString = "testSecretKeyForJwtTokenGenerationAndValidationMinimum32Characters";
        long accessTokenValidity = 3600000L;

        Field secretKeyStringField = JwtUtil.class.getDeclaredField("secretKeyString");
        secretKeyStringField.setAccessible(true);
        secretKeyStringField.set(jwtUtil, secretKeyString);

        Field accessTokenValidityField = JwtUtil.class.getDeclaredField("accessTokenValidity");
        accessTokenValidityField.setAccessible(true);
        accessTokenValidityField.set(jwtUtil, accessTokenValidity);

        // @PostConstruct 메서드 수동 호출
        jwtUtil.init();

        testSecretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void generateAccessToken_Success() {
        // When
        String token = jwtUtil.generateAccessToken(testEmail);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 header.payload.signature 구조
    }

    @Test
    @DisplayName("생성된 토큰이 null이 아님")
    void generateAccessToken_NotNull() {
        // When
        String token = jwtUtil.generateAccessToken(testEmail);

        // Then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("유효한 토큰 검증 성공")
    void validateToken_ValidToken_ReturnsTrue() {
        // Given
        String token = jwtUtil.generateAccessToken(testEmail);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패")
    void validateToken_ExpiredToken_ReturnsFalse() {
        // Given - 이미 만료된 토큰 생성 (만료 시간을 과거로 설정)
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 1000); // 1초 전 만료

        String expiredToken = Jwts.builder()
                .subject(testEmail)
                .issuedAt(new Date(now.getTime() - 2000))
                .expiration(expiredDate)
                .signWith(testSecretKey)
                .compact();

        // When
        boolean isValid = jwtUtil.validateToken(expiredToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("변조된 토큰 검증 실패")
    void validateToken_TamperedToken_ReturnsFalse() {
        // Given
        String validToken = jwtUtil.generateAccessToken(testEmail);
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "XXXXX";

        // When
        boolean isValid = jwtUtil.validateToken(tamperedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 형식의 토큰 검증 실패")
    void validateToken_MalformedToken_ReturnsFalse() {
        // Given
        String malformedToken = "invalid.token.format";

        // When
        boolean isValid = jwtUtil.validateToken(malformedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰 검증 실패")
    void validateToken_EmptyToken_ReturnsFalse() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtUtil.validateToken(emptyToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("토큰에서 이메일 추출 성공")
    void getEmailFromToken_Success() {
        // Given
        String token = jwtUtil.generateAccessToken(testEmail);

        // When
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        // Then
        assertThat(extractedEmail).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("여러 사용자의 토큰에서 각각 올바른 이메일 추출")
    void getEmailFromToken_MultipleUsers_Success() {
        // Given
        String email1 = "user1@example.com";
        String email2 = "user2@example.com";
        String email3 = "user3@example.com";

        String token1 = jwtUtil.generateAccessToken(email1);
        String token2 = jwtUtil.generateAccessToken(email2);
        String token3 = jwtUtil.generateAccessToken(email3);

        // When & Then
        assertThat(jwtUtil.getEmailFromToken(token1)).isEqualTo(email1);
        assertThat(jwtUtil.getEmailFromToken(token2)).isEqualTo(email2);
        assertThat(jwtUtil.getEmailFromToken(token3)).isEqualTo(email3);
    }

    @Test
    @DisplayName("변조된 토큰에서 이메일 추출 시 예외 발생")
    void getEmailFromToken_TamperedToken_ThrowsException() {
        // Given
        String validToken = jwtUtil.generateAccessToken(testEmail);
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "XXXXX";

        // When & Then
        assertThatThrownBy(() -> jwtUtil.getEmailFromToken(tamperedToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("토큰 생성과 검증 통합 테스트")
    void generateAndValidate_IntegrationTest() {
        // Given
        String email = "integration@test.com";

        // When
        String token = jwtUtil.generateAccessToken(email);
        boolean isValid = jwtUtil.validateToken(token);
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        // Then
        assertThat(token).isNotNull();
        assertThat(isValid).isTrue();
        assertThat(extractedEmail).isEqualTo(email);
    }

}
