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
    private final UserService userService;
}