package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/transfers")
    public List<AccountResponseDTO> getAccounts(@RequestParam String startDate,
        @RequestParam String endDate) {
        return accountService.getAccountTransactions(startDate, endDate);
    }

    @GetMapping()
    public List<AccountDTO> getUserAccounts() {
        return accountService.getUserAccounts();
    }
}