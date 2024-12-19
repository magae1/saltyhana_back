package com.saltyhana.saltyhanaserver.util;

import com.saltyhana.saltyhanaserver.entity.Goal;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class GoalUtil {
    public static Long calculatePercentage(Goal goal) {
        LocalDateTime now = LocalDateTime.now();

        // 시작 전인 경우
        if (now.isBefore(goal.getStartAt())) {
            return 0L;
        }

        // 이미 종료된 경우
        if (now.isAfter(goal.getEndAt()) || goal.isEnded()) {
            return 100L;
        }

        // 총 기간(일수) 계산
        long totalDays = ChronoUnit.DAYS.between(goal.getStartAt(), goal.getEndAt());
        if (totalDays == 0) { // 시작일과 종료일이 같은 경우
            return 100L;
        }

        // 경과 기간(일수) 계산
        long elapsedDays = ChronoUnit.DAYS.between(goal.getStartAt(), now);

        // 진행률 계산 (소수점 반올림)
        return Math.round((double) elapsedDays / totalDays * 100);
    }
}
