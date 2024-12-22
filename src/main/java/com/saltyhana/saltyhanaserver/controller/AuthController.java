package com.saltyhana.saltyhanaserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.dto.ExistsResponseDTO;
import com.saltyhana.saltyhanaserver.dto.IdResponse;
import com.saltyhana.saltyhanaserver.dto.IdResponse.IdResponseBuilder;
import com.saltyhana.saltyhanaserver.dto.TokenPairResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.EmailForm;
import com.saltyhana.saltyhanaserver.dto.form.IdentifierForm;
import com.saltyhana.saltyhanaserver.dto.form.LoginForm;
import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.dto.form.TokenRefreshForm;
import com.saltyhana.saltyhanaserver.service.AuthService;

@RestController
@RequestMapping("${api_prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public TokenPairResponseDTO login(LoginForm loginForm) {
      return authService.generateTokenPairWithLoginForm(loginForm);
    }

    @PostMapping("/refresh")
    public TokenPairResponseDTO refresh(TokenRefreshForm refreshForm) {
        return authService.refreshTokenPair(refreshForm.getRefreshToken());
    }

    @PostMapping("/signup")
    public IdResponse<Long> signup(SignUpForm signUpForm) {
        Long id = authService.signUp(signUpForm);
        IdResponseBuilder<Long> builder = IdResponse.builder();
        return builder.id(id).build();
    }

    @PostMapping("/unsubscribe")
    public void unsubscribe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        authService.unsubscribe(userId);
    }

    @PostMapping("/check-identifier")
    public ExistsResponseDTO checkIdentifier(IdentifierForm identifierForm) {
        return authService.checkIdentifier(identifierForm);
    }

    @PostMapping("/check-email")
    public ExistsResponseDTO checkIdentifier(EmailForm emailForm) {
        return authService.checkEmail(emailForm);
    }
}
