package com.saltyhana.saltyhanaserver.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;
import com.saltyhana.saltyhanaserver.provider.JWTProvider;


@Log4j2
public class AuthFilter extends OncePerRequestFilter {
    //TODO: auth 구현 이후 필터 재설정
    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        getAuthentication(authorization).ifPresent(authentication ->
            SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }

    private Optional<Authentication> getAuthentication(String authenticationHeader) {
        try {
            if (authenticationHeader == null) {
                throw new InvalidJWTException("헤더가 비어있습니다.");
            } else if(!authenticationHeader.startsWith("Bearer ")) {
                throw new InvalidJWTException("유효하지 않은 헤더 형식입니다.");
            }

            String accessToken = authenticationHeader.substring(7);
            Map<String, ?> claims = JWTProvider.parseAccessToken(accessToken);
            String id = claims.get("id").toString();
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            return Optional.of(new UsernamePasswordAuthenticationToken(id, accessToken, authorities));
        } catch (InvalidJWTException e) {
            log.warn(e);
        }
        return Optional.empty();
    }
}
