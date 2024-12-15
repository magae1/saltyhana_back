package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.MyPageUpdateDTO;
import com.saltyhana.saltyhanaserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userMyPageService;

    // 마이페이지 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<MyPageResponseDTO> getMyPage(@PathVariable String id) {
        try {
            MyPageResponseDTO response = userMyPageService.getUserInfo(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 마이페이지 정보 수정
    @PutMapping("/mypage/{id}")
    public ResponseEntity<MyPageResponseDTO> updateMyPage(
            @PathVariable String id,
            @RequestBody MyPageUpdateDTO updateDTO) {
        try {
            MyPageResponseDTO response = userMyPageService.updateUserInfo(id, updateDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}