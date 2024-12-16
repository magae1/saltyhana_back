package com.saltyhana.saltyhanaserver.provider;


import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;


public class JWTProviderTest {
    @Test
    @DisplayName("엑세스 토큰 기본 테스트")
    public void testAccessTokenGeneration() {
        Map<String, Object> claims = Map.of("username", "test-user","id", 1);
        Date now = new Date();
        String token = JWTProvider.generateAccessToken(claims, now);

        assertDoesNotThrow(() -> {
            Map<String, ?> result = JWTProvider.parseAccessToken(token);
            assertEquals(claims.get("username"), result.get("username"));
            assertEquals(claims.get("id"), result.get("id"));
        });
    }

    @Test
    @DisplayName("엑세스 토큰 만료 테스트")
    public void testAccessTokenExpiration() {
        Map<String, Object> claims = Map.of("id", 1);
        Date now = new Date();
        Date old = new Date(now.getTime() - (JWTProvider.ACCESS_TOKEN_EXPIRATION_INTERVAL + 1));
        String token = JWTProvider.generateAccessToken(claims, old);
        assertThrows(InvalidJWTException.class, () -> {
            JWTProvider.parseAccessToken(token);
        });
    }

    @Test
    @DisplayName("잘못된 엑세스 토큰 테스트")
    public void testInvalidAccessToken() {
        String token = "1231231223123";
        assertThrows(InvalidJWTException.class, () -> {
            JWTProvider.parseAccessToken(token);
        });
    }

    @Test
    @DisplayName("잘못된 토큰 타입에 대한 테스트")
    public void testWrongTokenType() {
        Map<String, Object> claims = Map.of("id", 1);
        String refreshToken = JWTProvider.generateRefreshToken(claims, new Date());
        assertThrows(InvalidJWTException.class, () -> {
            JWTProvider.parseAccessToken(refreshToken);
        });
    }
}
