package com.saltyhana.saltyhanaserver.provider;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;


public class JWTProvider {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final String SUBJECT = "richhana";

    private final long accessTokenExpirationInterval;
    private final long refreshTokenExpirationInterval;

    private static final String TOKEN_TYPE = "type";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    public JWTProvider(long accessTokenExpirationInterval, long refreshTokenExpirationInterval) {
        this.accessTokenExpirationInterval = accessTokenExpirationInterval;
        this.refreshTokenExpirationInterval = refreshTokenExpirationInterval;
    }

    public String generateAccessToken(Map<String, ?> claims, Date date) {
        HashMap<String, Object> claimsMap = new HashMap<>(claims);
        claimsMap.put(TOKEN_TYPE, ACCESS_TOKEN);
        return generateToken(claimsMap, date, accessTokenExpirationInterval);
    }

    public String generateRefreshToken(Map<String, ?> claims, Date date) {
        HashMap<String, Object> claimsMap = new HashMap<>(claims);
        claimsMap.put(TOKEN_TYPE, REFRESH_TOKEN);
        return generateToken(claimsMap, date, refreshTokenExpirationInterval);
    }

    private String generateToken(Map<String, ?> claims, Date date, long interval) {
        Date expiresAt = new Date(date.getTime() + interval * 1000);
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

    public Map<String, ?> parseAccessToken(String token) throws InvalidJWTException {
        Map<String, ?> claims = parseClaims(token);
        if (!claims.get(TOKEN_TYPE).equals(ACCESS_TOKEN)) {
            throw new InvalidJWTException("유효하지 않은 토큰입니다.");
        }
        return claims;
    }

    public Map<String, ?> parseRefreshToken(String token) throws InvalidJWTException {
        Map<String, ?> claims = parseClaims(token);
        if (!claims.get(TOKEN_TYPE).equals(REFRESH_TOKEN)) {
            throw new InvalidJWTException();
        }
        return claims;
    }

    public long getRefreshTokenExpirationInterval() {
        return refreshTokenExpirationInterval;
    }

    public long getAccessTokenExpirationInterval() {
        return accessTokenExpirationInterval;
    }

    private Map<String, ?> parseClaims(String token) throws InvalidJWTException {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJWTException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidJWTException();
        }
    }
}
