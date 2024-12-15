package com.saltyhana.saltyhanaserver.provider;


import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;


public class JWTProvider {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final String SUBJECT = "richhana";
    // 밀리초 기준
    public static final long ACCESS_TOKEN_EXPIRATION_INTERVAL = 1000 * 60 * 5;
    public static final long REFRESH_TOKEN_EXPIRATION_INTERVAL = 1000 * 60 * 60 * 24;

    private JWTProvider() {}

    public static String generateAccessToken(Map<String, ?> claims, Date date) {
        return generateToken(claims, date, ACCESS_TOKEN_EXPIRATION_INTERVAL);
    }

    public static String generateRefreshToken(Map<String, ?> claims, Date date) {
        return generateToken(claims, date, REFRESH_TOKEN_EXPIRATION_INTERVAL);
    }

    private static String generateToken(Map<String, ?> claims, Date date, long interval) {
        Date expiresAt = new Date(date.getTime() + interval);
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(SUBJECT)
                .claims(claims)
                .issuedAt(date)
                .expiration(expiresAt)
                .signWith(SECRET_KEY)
                .compact();
    }


    public static Map<String, ?> parseClaims(String token) throws InvalidJWTException {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJWTException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidJWTException("유효하지 않은 JWT입니다.");
        }
    }
}
