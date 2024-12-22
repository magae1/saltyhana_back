package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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

    private User testUser;

    @BeforeAll
    void setup() {
        // 테스트용 사용자 생성
        testUser = new User();
        testUser.setIdentifier("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("$2a$10$xKZ5WkXIbCD6qtUBeempGedAAJNai3sWjuy0vkjTEtwwhBdnlR9L");
        testUser.setBirth(LocalDate.of(1999, 1, 1));
        testUser.setName("테스트");
        testUser.setActive(true);
        testUser = userRepository.save(testUser);
    }

    @Test
    @Order(1)
    void testUnsubscribe() {
        // Authentication 객체 생성 (사용자 ID를 principal로 설정)
        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser.getId(), null);

        // 구독 취소 호출
        authService.unsubscribe(authentication);

        // 사용자 상태 확인
        Optional<User> updatedUser = userRepository.findById(testUser.getId());
        assertTrue(updatedUser.isPresent());
        assertFalse(updatedUser.get().isActive(), "사용자가 비활성화 상태로 변경되지 않았습니다.");
    }
}
