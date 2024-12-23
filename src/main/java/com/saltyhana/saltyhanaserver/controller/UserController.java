package com.saltyhana.saltyhanaserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SimpleProfileResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.MyPageUpdateForm;
import com.saltyhana.saltyhanaserver.service.UserService;


@Log4j2
@RestController
@RequestMapping("${api_prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public MyPageResponseDTO getMyPage() {
        Long userId = getUserId();
        return userService.getUserInfo(userId);
    }

    @PatchMapping("/me")
    public MyPageResponseDTO updateMyPage(@RequestBody MyPageUpdateForm updateForm) {
        Long userId = getUserId();
        return userService.updateUserInfo(userId, updateForm);
    }

    @GetMapping("/me/simple")
    public SimpleProfileResponseDTO getSimpleProfile() {
        Long userId = getUserId();
        return userService.getSimpleProfile(userId);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}