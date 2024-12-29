package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.WeekDayType;
import com.saltyhana.saltyhanaserver.dto.WeekdayCalendarResponseDTO;
import com.saltyhana.saltyhanaserver.repository.GoalRepository;
import com.saltyhana.saltyhanaserver.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekdayCalendarService {
    private final ProgressRepository progressRepository;
    private final GoalRepository goalRepository;

    public WeekdayCalendarResponseDTO getWeekdayCalendar(String goalName) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tenDaysAgo = today.minusDays(10);

        Long goalId = goalRepository.findIdByName(goalName);
        List<LocalDateTime> addedDates = progressRepository.findAddAtDatesByGoalIdAndDateRange(goalId, tenDaysAgo, today);

        List<WeekDayType> weekdays = new ArrayList<>();

        // 오늘부터 10일 전까지의 날짜 확인
        for (int i = 10; i >= 0; i--) {
            LocalDate currentDate = today.toLocalDate().minusDays(i);

            boolean isAchieve = addedDates.stream()
                    .map(LocalDateTime::toLocalDate)
                    .anyMatch(addedDate -> addedDate.equals(currentDate));

            weekdays.add(WeekDayType.builder()
                    .date(currentDate)
                    .isAchieve(isAchieve)
                    .build());
        }

        return WeekdayCalendarResponseDTO.builder()
                .weekday(weekdays)
                .build();
    }
}
