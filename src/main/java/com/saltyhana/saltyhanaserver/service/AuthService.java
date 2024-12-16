package com.saltyhana.saltyhanaserver.service;


import java.util.Date;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.saltyhana.saltyhanaserver.dto.TokenPairResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.LoginForm;
import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.exception.WrongPasswordException;
import com.saltyhana.saltyhanaserver.mapper.AuthMapper;
import com.saltyhana.saltyhanaserver.provider.JWTProvider;
import com.saltyhana.saltyhanaserver.repository.UserAuthRepository;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthRepository userAuthRepo;
    private final PasswordEncoder passwordEncoder;

    public TokenPairResponseDTO generateTokenPairWithLoginForm(LoginForm loginForm) throws ResponseStatusException {
        User user = userAuthRepo.findByIdentifier(loginForm.getIdentifier()).orElse(null);
        if (user == null) {
            throw new NotFoundException("사용자");
        } else if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        Map<String, ?> userClaims = Map.of("identifier", loginForm.getIdentifier(),
                "id", user.getId());
        return generateTokenPair(userClaims);
    }

    public TokenPairResponseDTO refreshTokenPair(String oldRefreshToken) throws ResponseStatusException {
        try {
            Map<String, ?> claims = JWTProvider.parseClaims(oldRefreshToken);
            return generateTokenPair(claims);
        } catch (InvalidJWTException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
        }
    }

    public Long signUp(SignUpForm signUpForm) throws ResponseStatusException {
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            throw new WrongPasswordException();
        }

        User user = AuthMapper.toEntity(signUpForm, passwordEncoder.encode(signUpForm.getPassword()));
        User savedUser = userAuthRepo.save(user);
        return savedUser.getId();
    }

    private TokenPairResponseDTO generateTokenPair(Map<String,?> claims) {
        Date now = new Date();
        String accessToken = JWTProvider.generateAccessToken(claims, now);
        String refreshToken = JWTProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
