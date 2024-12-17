package com.saltyhana.saltyhanaserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.MyPageUpdateForm;
import com.saltyhana.saltyhanaserver.service.UserService;


@Log4j2
@RestController
@PreAuthorize("hasAnyRole('ROLE_USER')")
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
    public MyPageResponseDTO updateMyPage(MyPageUpdateForm updateForm) {
        Long userId = getUserId();
        return userService.updateUserInfo(userId, updateForm);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}