package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TransferDTO;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.repository.AccountRepository;
import com.saltyhana.saltyhanaserver.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferService transferService;

    public List<AccountResponseDTO> getAccountTransactions(AccountRequestDTO accountRequestDTO) {
        Long userId = accountRequestDTO.getUserId();
        String startDate = accountRequestDTO.getStartDate();
        String endDate = accountRequestDTO.getEndDate();

        List<AccountDTO> accounts = accountRepository.findByUserId(userId);

        List<AccountResponseDTO> accountResponse = new ArrayList<>();

        accounts.forEach( account -> {
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
