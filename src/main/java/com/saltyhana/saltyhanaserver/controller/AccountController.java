package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public List<AccountResponseDTO> getAccounts(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.getAccountTransactions(accountRequestDTO);
    }
}
