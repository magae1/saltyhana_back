package com.saltyhana.saltyhanaserver.util;

import com.saltyhana.saltyhanaserver.entity.Transfer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DashboardUtil {
    public static Integer calculateDailyAmount(LocalDateTime startAt, LocalDateTime endAt, double goalAmount){
        long totalDays = ChronoUnit.DAYS.between(startAt, endAt) + 1;
        return (int)Math.round(goalAmount / totalDays);
    }

    public static boolean checkIfAchieved(List<Transfer> transfers, Integer dailyAmount, LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
        for (Transfer t : transfers) {
            LocalDate transTimeLocalDate = t.getTranTime().toLocalDate();
            if (transTimeLocalDate.equals(localDate) && t.getTranAmt() == dailyAmount) {
                return true;
            }
        }
        return false;
    }

    public static Long calculatePercentage(Long dailyAmount, Long goalAmount){
        return Math.round((double) dailyAmount / goalAmount * 100);
    }
}
