package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.AccountResponseDTO;
import com.saltyhana.saltyhanaserver.dto.TransferDTO;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.repository.AccountRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import com.saltyhana.saltyhanaserver.util.StringFormatter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final TransferService transferService;

  public List<AccountDTO> getUserAccounts(Long userId) {
    return accountRepository.findAllByUserId(userId);
  }

  public List<AccountResponseDTO> getAccountTransactions(Long userId, LocalDate startDate,
      LocalDate endDate) {

    List<AccountDTO> accounts = accountRepository.findAllByUserId(userId);

    List<AccountResponseDTO> accountResponse = new ArrayList<>();

    accounts.forEach(account -> {
      List<TransferDTO> transferList = transferService.getDailyTransactions(
          account.getId(),
          startDate,
          endDate
      );

      List<TransferDTO> filledTransferList = new ArrayList<>();
      LocalDate currentDate = startDate;

      // Process all dates in the range
      TransferDTO lastTransfer = null;
      while (!currentDate.isAfter(endDate)) {
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