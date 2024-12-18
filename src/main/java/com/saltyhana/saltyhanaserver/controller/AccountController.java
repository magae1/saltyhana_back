package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public List<AccountResponseDTO> getAccounts(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.getAccountTransactions(accountRequestDTO);
    }
}
