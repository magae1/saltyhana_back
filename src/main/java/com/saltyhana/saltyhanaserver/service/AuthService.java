package com.saltyhana.saltyhanaserver.service;


import java.util.Date;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.saltyhana.saltyhanaserver.dto.ExistsResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TokenPairResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.EmailForm;
import com.saltyhana.saltyhanaserver.dto.form.IdentifierForm;
import com.saltyhana.saltyhanaserver.dto.form.LoginForm;
import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.exception.WrongPasswordException;
import com.saltyhana.saltyhanaserver.mapper.UserMapper;
import com.saltyhana.saltyhanaserver.provider.JWTProvider;
import com.saltyhana.saltyhanaserver.repository.UserRepository;

import static com.saltyhana.saltyhanaserver.util.AuthUtil.extractTokenFromRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenPairResponseDTO generateTokenPairWithLoginForm(LoginForm loginForm)
            throws ResponseStatusException {
        User user = userRepository.findByIdentifier(loginForm.getIdentifier()).orElse(null);
        if (user == null) {
            throw new NotFoundException("사용자");
        } else if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        Map<String, Object> userClaims = Map.of("identifier", loginForm.getIdentifier(),
                "id", user.getId());
        return generateTokenPair(userClaims);
    }

    public TokenPairResponseDTO refreshTokenPair(String oldRefreshToken)
            throws ResponseStatusException {
        try {
            Map<String, ?> claims = JWTProvider.parseRefreshToken(oldRefreshToken);
            return generateTokenPair(claims);
        } catch (InvalidJWTException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Long signUp(SignUpForm signUpForm) throws ResponseStatusException {
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            throw new WrongPasswordException();
        }

        User user = UserMapper.toEntity(signUpForm, passwordEncoder.encode(signUpForm.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        String token = extractTokenFromRequest(request);
        if (StringUtils.hasText(token)) {
            // 토큰 상태 확인 후 로그아웃 처리
            if (JWTProvider.isTokenActive(token)) {
                JWTProvider.invalidateToken(token); // 로그아웃 시 토큰 비활성화
            }
        }
    }

    public void unsubscribe(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }

        Long userId = (Long) authentication.getPrincipal();     // 사용자 ID를 Authentication에서 가져옴
        userRepository.findById(userId).ifPresentOrElse(user -> {
            user.setActive(false);
            userRepository.save(user);
        }, () -> {
            throw new NotFoundException("사용자");
        });
    }

    public ExistsResponseDTO checkEmail(EmailForm emailForm) {
        boolean exists = checkEmailExists(emailForm.getEmail());
        return ExistsResponseDTO.builder()
                .exists(exists)
                .build();
    }

    public ExistsResponseDTO checkIdentifier(IdentifierForm identifierForm) {
        boolean exists = checkIdentifierExists(identifierForm.getIdentifier());
        return ExistsResponseDTO.builder()
                .exists(exists)
                .build();
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkIdentifierExists(String identifier) {
        return userRepository.existsByIdentifier(identifier);
    }

    private TokenPairResponseDTO generateTokenPair(Map<String, ?> claims) {
        Date now = new Date();
        String accessToken = JWTProvider.generateAccessToken(claims, now);
        String refreshToken = JWTProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
