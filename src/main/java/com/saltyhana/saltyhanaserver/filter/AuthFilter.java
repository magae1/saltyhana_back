package com.saltyhana.saltyhanaserver.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthFilter extends OncePerRequestFilter {
    //TODO: auth 구현 이후 필터 재설정
    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        return true;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
