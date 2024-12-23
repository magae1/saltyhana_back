package com.saltyhana.saltyhanaserver.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.service.AccountService;


@Log4j2
@RestController
@RequestMapping("${api_prefix}/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/transfers")
    public List<AccountResponseDTO> getAccounts(@RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {
        LocalDate now = LocalDate.now();
        if (endDate == null || endDate.isAfter(now)) {
            endDate = now;
        }
        if (startDate == null) {
            startDate = now.minusMonths(1);
        }
        log.info("{} ~ {}에서의 입출금내역", startDate, endDate);
        Long userId = getUserId();
        return accountService.getAccountTransactions(userId, startDate, endDate);
    }

    @GetMapping
    public List<AccountDTO> getUserAccounts() {
        Long userId = getUserId();
        return accountService.getUserAccounts(userId);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}