package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TransferDTO;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.repository.AccountRepository;
import com.saltyhana.saltyhanaserver.util.StringFormatter;

import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;
    private TransferService transferService;

    public List<AccountResponseDTO> getAccountTransactions(AccountRequestDTO accountRequestDTO) {
        Long userId = accountRequestDTO.getUserId();
        String startDate = accountRequestDTO.getStartDate();
        String endDate = accountRequestDTO.getEndDate();

        List<AccountDTO> accounts = accountRepository.findByUserId(userId);

        List<AccountResponseDTO> accountResponse = new ArrayList<>();

        System.out.println("userId: " + userId);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + startDate);

        accounts.forEach(account -> {
            List<TransferDTO> transferList = transferService.getDailyTransactions(
                    account.getId(),
                    startDate,
                    endDate
            );

            System.out.println("TransferList: "+ transferList);
            accountResponse.add(
                    AccountResponseDTO.builder()
                            .accountNumber(StringFormatter.toAccountNumber(account.getAccountNumber()))
                            .accountAlias(account.getAccountAlias())
                            .accountBalance(account.getAccountBalance())
                            .transferList(transferList)
                            .build()
            );
        });

        return accountResponse;
    }
}
