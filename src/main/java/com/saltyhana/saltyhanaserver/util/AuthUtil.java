package com.saltyhana.saltyhanaserver.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class AuthUtil {
    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
