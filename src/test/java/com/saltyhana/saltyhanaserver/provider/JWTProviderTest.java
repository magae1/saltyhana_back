package com.saltyhana.saltyhanaserver.provider;


import java.util.Date;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;


@Log4j2
@SpringBootTest
public class JWTProviderTest {

    @Autowired
    private JWTProvider jwtProvider;


    @Test
    @DisplayName("엑세스 토큰 기본 테스트")
    public void testAccessTokenGeneration() {
        Map<String, Object> claims = Map.of("username", "test-user","id", 1);
        Date now = new Date();
        String token = jwtProvider.generateAccessToken(claims, now);
        log.info("Access Token: {}", token);
        assertDoesNotThrow(() -> {
            Map<String, ?> result = jwtProvider.parseAccessToken(token);
            assertEquals(claims.get("username"), result.get("username"));
            assertEquals(claims.get("id"), result.get("id"));
        });
    }

    @Test
    @DisplayName("엑세스 토큰 만료 테스트")
    public void testAccessTokenExpiration() {
        Map<String, Object> claims = Map.of("id", 1);
        Date now = new Date();
        Date old = new Date(
            now.getTime() - (jwtProvider.getAccessTokenExpirationInterval() * 1000 + 1));
        String token = jwtProvider.generateAccessToken(claims, old);
        log.info("Access Token: {}", token);
        assertThrows(InvalidJWTException.class, () -> {
            jwtProvider.parseAccessToken(token);
        });
    }

    @Test
    @DisplayName("잘못된 엑세스 토큰 테스트")
    public void testInvalidAccessToken() {
        String token = "1231231223123";
        assertThrows(InvalidJWTException.class, () -> {
            jwtProvider.parseAccessToken(token);
        });
    }

    @Test
    @DisplayName("잘못된 토큰 타입에 대한 테스트")
    public void testWrongTokenType() {
        Map<String, Object> claims = Map.of("id", 1);
        String refreshToken = jwtProvider.generateRefreshToken(claims, new Date());
        log.info("Refresh Token: {}", refreshToken);
        assertThrows(InvalidJWTException.class, () -> {
            jwtProvider.parseAccessToken(refreshToken);
        });
    }
}
