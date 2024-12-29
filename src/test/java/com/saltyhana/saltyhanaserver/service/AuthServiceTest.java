package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.repository.UserRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeAll
    void setup() {
        // 테스트용 사용자 생성
        testUser = User.builder()
            .identifier("testuser")
            .email("testuser@example.com")
            .password(passwordEncoder.encode("test-password"))
            .birth(LocalDate.of(1990, 1, 1))
            .name("테스트")
            .build();
        testUser = userRepository.save(testUser);
    }

    @Order(1)
    void testUnsubscribe() {
        Long userId = testUser.getId();
        // 구독 취소 호출
        authService.unsubscribe(userId);

        // 사용자 상태 확인
        Optional<User> updatedUser = userRepository.findById(testUser.getId());
        assertTrue(updatedUser.isPresent());
        assertFalse(updatedUser.get().isActive(), "사용자가 비활성화 상태로 변경되지 않았습니다.");
    }
}
