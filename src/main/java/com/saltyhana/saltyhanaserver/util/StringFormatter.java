package com.saltyhana.saltyhanaserver.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StringFormatter {

    // accountNumber를 "000-000000-00-000" 형식으로 변환하는 메서드
    public static String toAccountNumber(Long accountNumber) {
            // accountNumber를 문자열로 변환 후 0을 채워서 포맷팅
            return String.format("%03d-%06d-%02d-%03d",
                    (accountNumber / 1000000000000L),        // 000 (앞 세 자리)
                    (accountNumber / 1000) % 1000000,         // 000000 (중간 6자리)
                    (accountNumber / 10) % 100,               // 00 (끝 두 자리)
                    accountNumber % 10);                      // 000 (마지막 3자리)
    }

    public static List<LocalDateTime> toLocalDateTime(String startDateString, String endDateString) {
        // 날짜 문자열을 LocalDateTime으로 변환 (시간은 00:00:00으로 설정)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateString + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString + "T23:59:59", formatter);

        return List.of(startDate, endDate);
    }
}
