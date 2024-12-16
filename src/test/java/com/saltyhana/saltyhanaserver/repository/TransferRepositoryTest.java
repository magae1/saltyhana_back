package com.saltyhana.saltyhanaserver.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransferRepositoryTest {

    @Autowired
    private TransferRepository transferRepository;

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
