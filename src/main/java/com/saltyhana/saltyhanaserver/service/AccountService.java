package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.AccountRequestDTO;
import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TransferDTO;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.repository.AccountRepository;
import com.saltyhana.saltyhanaserver.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.saltyhana.saltyhanaserver.util.StringFormatter.toLacalDate;

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

        List<LocalDate> dates = toLacalDate(startDate, endDate);

        accounts.forEach(account -> {
            List<TransferDTO> transferList = transferService.getDailyTransactions(
                    account.getId(),
                    startDate,
                    endDate
            );

            List<TransferDTO> filledTransferList = new ArrayList<>();
            LocalDate currentDate = dates.get(0);

            // Process all dates in the range
            TransferDTO lastTransfer = null;
            while (!currentDate.isAfter(dates.get(1))) {
                boolean found = false;
                for (TransferDTO transfer : transferList) {
                    if (transfer.getDate().equals(currentDate.toString())) {
                        filledTransferList.add(transfer);
                        lastTransfer = transfer;
                        found = true;
                        break;
                    }
                }

                // 계좌내역이 없을 경우 입출금 총액 0으로 입력
                if (!found) {
                    TransferDTO dummyTransfer = TransferDTO.builder()
                            .date(currentDate.toString())
                            .balance(lastTransfer != null ? lastTransfer.getBalance() : 0)
                            .totalDeposit(0L)
                            .totalWithdrawal(0L)
                            .build();
                    filledTransferList.add(dummyTransfer);
                    lastTransfer = dummyTransfer;
                }

                currentDate = currentDate.plusDays(1);
            }

            // 가장 최근 계좌 잔액으로 입력
            AccountResponseDTO response = AccountResponseDTO.builder()
                    .accountNumber(StringFormatter.toAccountNumber(account.getAccountNumber()))
                    .accountAlias(account.getAccountAlias())
                    .accountBalance(filledTransferList.get(filledTransferList.size() - 1).getBalance())
                    .transferList(filledTransferList)
                    .build();

            accountResponse.add(response);
        });

        return accountResponse;
    }
}
