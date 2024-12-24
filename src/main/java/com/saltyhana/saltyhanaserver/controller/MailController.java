package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.MailDTO;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.service.MailService;
import com.saltyhana.saltyhanaserver.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api_prefix}/password")
@RequiredArgsConstructor
public class MailController {
    private final UserService userService;
    private final MailService mailService;

    @Operation(summary = "비밀번호 변경 요청", description = "비밀번호 변경을 위해 이메일을 전송합니다. 임시 비밀번호가 이메일로 전송됩니다.")
    @GetMapping("/change")
    public void checkEmail(@RequestParam("email") String email) {
        if(!userService.emailExist(email)) {
            throw new NotFoundException("이메일");
        }

        try {
            String tmpPassword = userService.getTmpPassword();
            userService.updateTmpPassword(tmpPassword, email);

            MailDTO mail = mailService.createMail(tmpPassword, email);
            mailService.sendMail(mail);
        } catch (Exception e) {
            // 메일 발송 오류 등 기타 예외 처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
        }
    }
}