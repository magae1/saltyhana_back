package com.saltyhana.saltyhanaserver.service;


import java.util.Date;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.saltyhana.saltyhanaserver.dto.ExistsResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TokenPairResponseDTO;
import com.saltyhana.saltyhanaserver.dto.UserDTO;
import com.saltyhana.saltyhanaserver.dto.form.EmailForm;
import com.saltyhana.saltyhanaserver.dto.form.IdentifierForm;
import com.saltyhana.saltyhanaserver.dto.form.LoginForm;
import com.saltyhana.saltyhanaserver.dto.form.SignUpForm;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.AlreadyExistsException;
import com.saltyhana.saltyhanaserver.exception.InvalidJWTException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.exception.WrongPasswordException;
import com.saltyhana.saltyhanaserver.mapper.UserMapper;
import com.saltyhana.saltyhanaserver.provider.JWTProvider;
import com.saltyhana.saltyhanaserver.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

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
            Map<String, ?> claims = jwtProvider.parseRefreshToken(oldRefreshToken);
            return generateTokenPair(claims);
        } catch (InvalidJWTException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Long signUp(SignUpForm signUpForm) throws ResponseStatusException {
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            throw new WrongPasswordException();
        }
        String identifier = signUpForm.getIdentifier();
        String email = signUpForm.getEmail();
        Optional<User> user = userRepository.findInactiveUserByIdentifierAndEmail(identifier, email);
        if (user.isPresent()) {
            User existingUser = user.get();
            UserDTO existingUserDTO = UserMapper.toDTO(existingUser);
            existingUserDTO.setActive(true);
            return userRepository.save(UserMapper.toEntity(existingUserDTO)).getId();
        }

        if (checkIdentifierExists(identifier)) {
            throw new AlreadyExistsException("아이디");
        } else if (checkEmailExists(email)) {
            throw new AlreadyExistsException("이메일");
        }

        User savedUser = registerUser(signUpForm);
        return savedUser.getId();
    }

    public void unsubscribe(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(user -> {
            UserDTO userDTO = UserMapper.toDTO(user);
            userDTO.setActive(false);
            userRepository.save(UserMapper.toEntity(userDTO));
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
        String accessToken = jwtProvider.generateAccessToken(claims, now);
        String refreshToken = jwtProvider.generateRefreshToken(claims, now);
        return TokenPairResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User registerUser(SignUpForm signUpForm) {
        User user = UserMapper.toEntity(signUpForm, passwordEncoder.encode(signUpForm.getPassword()));
        return userRepository.save(user);
    }
}
