package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.TransferDTO;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Transfer;
import com.saltyhana.saltyhanaserver.repository.GoalRepository;
import com.saltyhana.saltyhanaserver.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.saltyhana.saltyhanaserver.util.DashboardUtil.calculateDailyAmount;
import static com.saltyhana.saltyhanaserver.util.StringFormatter.toLocalDateTime;

@Service
public class TransferService {
    @Autowired
    private final TransferRepository transferRepository;
    @Autowired
    private final GoalRepository goalRepository;
    @Autowired
    private ProgressService progressService;


    @Autowired
    public TransferService(TransferRepository transferRepository, GoalRepository goalRepository) {
        this.transferRepository = transferRepository;
        this.goalRepository = goalRepository;
    }

    public List<TransferDTO> getDailyTransactions(Long accountId, LocalDate startDate,
                                                  LocalDate endDate) {
        List<LocalDateTime> datetimes = toLocalDateTime(startDate, endDate);
        LocalDateTime startDateTime = datetimes.get(0);
        LocalDateTime endDateTime = datetimes.get(1);
        // 일일 거래 내역과 일일 잔액을 가져온다
        List<Object[]> dailyTransactions = transferRepository.findDailyTransactions(accountId,
                startDateTime, endDateTime);
        List<Object[]> dailyBalance = transferRepository.findDailyBalance(accountId, startDateTime,
                endDateTime);

        // 날짜별로 입금, 출금, 잔액을 기록하기 위한 맵
        Map<String, Long> totalDepositMap = new HashMap<>();
        Map<String, Long> totalWithdrawalMap = new HashMap<>();
        Map<String, Integer> balanceMap = new HashMap<>();

        // 거래 내역을 바탕으로 입금액과 출금액을 구한다
        for (Object[] transaction : dailyTransactions) {
            String date = transaction[0].toString().split("T")[0];
            Long totalDeposit = (Long) transaction[1];
            Long totalWithdrawal = (Long) transaction[2];

            totalDepositMap.put(date, totalDeposit);
            totalWithdrawalMap.put(date, totalWithdrawal);
        }

        // 일일 잔액을 맵에 저장 (최신 잔액으로 업데이트)
        for (Object[] balance : dailyBalance) {
            String date = balance[0].toString().split("T")[0];
            Integer afterBalanceAmt = (Integer) balance[1];
            balanceMap.put(date, afterBalanceAmt);
        }

        // 결과 리스트 생성
        List<TransferDTO> transferList = new ArrayList<>();

        // 날짜별로 잔액, 입금액, 출금액을 결합
        for (String date : balanceMap.keySet()) {
            Integer balance = balanceMap.get(date);
            Long totalDeposit = totalDepositMap.getOrDefault(date, 0L);
            Long totalWithdrawal = totalWithdrawalMap.getOrDefault(date, 0L);

            TransferDTO dto = new TransferDTO(date, balance, totalDeposit, totalWithdrawal);
            transferList.add(dto);
        }

        // 날짜순으로 정렬 (오름차순)
        transferList.sort(Comparator.comparing(TransferDTO::getDate));

        return transferList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkTransfer(Transfer transfer) {
        if (transfer.getInOutType() == 0) {

            String printedContent = transfer.getPrintedContent();

            Goal goal = goalRepository.findByName(printedContent).orElse(null);

             //하루에 이체되어야 하는 금액
            LocalDateTime startAt = goal.getStartAt();
            LocalDateTime endAt = goal.getEndAt();
            Integer dailyAmount = calculateDailyAmount(startAt, endAt, goal.getAmount());

            if(dailyAmount.equals(transfer.getTranAmt()))
                progressService.insertProgress(goal, dailyAmount, transfer.getTranTime());

        }
    }
}