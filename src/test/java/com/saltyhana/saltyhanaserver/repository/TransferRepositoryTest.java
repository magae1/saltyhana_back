package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.entity.Transfer;
import com.saltyhana.saltyhanaserver.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransferRepositoryTest {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Test
    public void setUp() {
        // 1. User 생성 및 저장
        User user = User.builder()
                .identifier("user1")
                .password("b82cbd65-aa93-45b4-831f-1a8559a9df80")
                .name("홍길동")
                .email("hong@example.com")
                .birth(LocalDate.of(1990, 1, 1))
                .build();
        userAuthRepository.save(user);

        // 2. Account 생성 및 저장
        Account account1 = Account.builder()
                .user(user)
                .fintechUseNum(123456)
                .accountAlias("주거래 하나 통장*")
                .bankCodeStd(352)
                .bankCodeSub(0)
                .bankName("하나은행")
                .accountNum(35200000000111L)
                .accountType("S")
                .accountBalance(2005333)
                .build();

        Account account2 = Account.builder()
                .user(user)
                .fintechUseNum(789012)
                .accountAlias("369 정기예금")
                .bankCodeStd(352)
                .bankCodeSub(0)
                .bankName("하나은행")
                .accountNum(35200000000222L)
                .accountType("S")
                .accountBalance(1523000)
                .build();

        Account account3 = Account.builder()
                .user(user)
                .fintechUseNum(345678)
                .accountAlias("부자씨 적금")
                .bankCodeStd(352)
                .bankCodeSub(0)
                .bankName("하나은행")
                .accountNum(35200000000333L)
                .accountType("S")
                .accountBalance(780000)
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);

        // 3. Transfer 데이터 생성 및 저장
        Transfer transfer1 = Transfer.builder()
                .account(account1)
                .tranTime(LocalDateTime.of(2024, 12, 1, 12, 30, 0))
                .inOutType(0)
                .printedContent("급여 입금")
                .tranAmt(500000)
                .afterBalanceAmt(2505333)
                .branchName("하나은행 강남지점")
                .build();

        Transfer transfer2 = Transfer.builder()
                .account(account1)
                .tranTime(LocalDateTime.of(2024, 12, 2, 15, 0, 0))
                .inOutType(1)
                .printedContent("카드 결제")
                .tranAmt(200000)
                .afterBalanceAmt(2305333)
                .branchName("하나은행 강남지점")
                .build();

        Transfer transfer3 = Transfer.builder()
                .account(account2)
                .tranTime(LocalDateTime.of(2024, 12, 3, 9, 0, 0))
                .inOutType(0)
                .printedContent("이자 지급")
                .tranAmt(300000)
                .afterBalanceAmt(1823000)
                .branchName("하나은행 본점")
                .build();

        Transfer transfer4 = Transfer.builder()
                .account(account2)
                .tranTime(LocalDateTime.of(2024, 12, 4, 16, 30, 0))
                .inOutType(1)
                .printedContent("ATM 인출")
                .tranAmt(500000)
                .afterBalanceAmt(1323000)
                .branchName("하나은행 송파지점")
                .build();

        Transfer transfer5 = Transfer.builder()
                .account(account3)
                .tranTime(LocalDateTime.of(2024, 12, 5, 10, 0, 0))
                .inOutType(0)
                .printedContent("적금 입금")
                .tranAmt(100000)
                .afterBalanceAmt(880000)
                .branchName("하나은행 신촌지점")
                .build();

        Transfer transfer6 = Transfer.builder()
                .account(account3)
                .tranTime(LocalDateTime.of(2024, 12, 6, 14, 0, 0))
                .inOutType(1)
                .printedContent("자동이체")
                .tranAmt(100000)
                .afterBalanceAmt(780000)
                .branchName("하나은행 강남지점")
                .build();

        transferRepository.save(transfer1);
        transferRepository.save(transfer2);
        transferRepository.save(transfer3);
        transferRepository.save(transfer4);
        transferRepository.save(transfer5);
        transferRepository.save(transfer6);
    }


    @Test
    @DisplayName("계좌 입출금 내역 조회 테스트")
    public void testFindDailyTransactions() throws Exception {
        // 주어진 날짜 범위 내에서 입출금 내역을 찾는 테스트
        String startDateString = "2024-12-01";
        String endDateString = "2024-12-15";

        // 날짜 문자열을 LocalDateTime으로 변환 (시간은 00:00:00으로 설정)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString + "T23:59:59", formatter);

        List<Object[]> transactions = transferRepository.findDailyTransactions(1L, startDate, endDate);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        Object[] transaction = transactions.get(0);
        assertEquals("2024-12-01", transaction[0].toString().split("T")[0]);  // 날짜가 맞는지 확인
        assertEquals(500000L, transaction[1]);  // 입금 총액
        assertEquals(0L, transaction[2]);  // 출금 총액
    }

    @Test
    @DisplayName("계좌 잔액 내역 조회 테스트")
    public void testFindDailyBalance() {
        // 주어진 날짜 범위 내에서 잔액 내역을 찾는 테스트
        String startDateString = "2024-12-01";
        String endDateString = "2024-12-15";

        // 날짜 문자열을 LocalDateTime으로 변환 (시간은 00:00:00으로 설정)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString + "T23:59:59", formatter);

        List<Object[]> dailyBalance = transferRepository.findDailyBalance(2L, startDate, endDate);

        assertNotNull(dailyBalance);
        assertEquals(2, dailyBalance.size());

        Object[] balance = dailyBalance.get(0);
        System.out.println(balance[0].toString());
        assertEquals("2024-12-03", balance[0].toString().split("T")[0]);  // 날짜가 맞는지 확인
        assertEquals(1823000, balance[1]);  // 마지막 잔액 확인
    }
}
