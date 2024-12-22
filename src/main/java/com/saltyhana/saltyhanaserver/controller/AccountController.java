package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public List<AccountResponseDTO> getAccounts(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.getAccountTransactions(accountRequestDTO);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getUserAccounts() {
        return accountService.getUserAccounts();
    }
}