package com.saltyhana.saltyhanaserver.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StringFormatter {

    // accountNumber를 "000-000000-00000" 형식으로 변환하는 메서드
    public static String toAccountNumber(Long accountNumber) {
        // accountNumber를 문자열로 변환 후 0을 채워서 포맷팅
        String accountNumberStr = String.format("%014d", accountNumber); // 15자리로 맞춘 후

        // 3개 구간으로 나누어 포맷팅
        return accountNumberStr.substring(0, 3) + "-" +       // 앞 3자리
                accountNumberStr.substring(3, 9) + "-" +       // 중간 6자리
                accountNumberStr.substring(9, 14);             // 마지막 5자리
    }

    public static List<LocalDateTime> toLocalDateTime(String startDateString, String endDateString) {
        // 날짜 문자열을 LocalDateTime으로 변환 (시간은 00:00:00으로 설정)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString + "T23:59:59", formatter);

        return List.of(startDate, endDate);
    }

    public static List<LocalDate> toLacalDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return List.of(start, end);
    }
}
