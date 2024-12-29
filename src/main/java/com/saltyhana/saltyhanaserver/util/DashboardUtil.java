package com.saltyhana.saltyhanaserver.util;

import com.saltyhana.saltyhanaserver.entity.Transfer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DashboardUtil {
    public static Integer calculateDailyAmount(LocalDateTime startAt, LocalDateTime endAt, double goalAmount){
        long totalDays = ChronoUnit.DAYS.between(startAt, endAt);
        return (int)Math.round(goalAmount / totalDays);
    }

    public static Long calculatePercentage(Long dailyAmount, Long goalAmount){
        return Math.round((double) dailyAmount / goalAmount * 100);
    }

    public static String formatGoalPeriod(LocalDateTime startAt, LocalDateTime endAt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedStart = startAt.format(formatter);
        String formattedEnd = endAt.format(formatter);
        return formattedStart + "~" + formattedEnd;
    }
}
