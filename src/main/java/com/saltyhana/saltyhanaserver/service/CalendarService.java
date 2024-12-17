package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.CalendarResponseDTO;
import com.saltyhana.saltyhanaserver.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    // 특정 사용자(userId)의 모든 목표를 조회
    public List<CalendarResponseDTO> getAllCalendarGoals(Long userId) {

        return calendarRepository.findByUserId(userId).stream().map(goal -> {

            return CalendarResponseDTO.builder()
                    .id(goal.getId())
                    .title(goal.getName())
                    .startDate(goal.getStartAt().toString())
                    .endDate(goal.getEndAt().toString())
                    .icon(goal.getIcon().getIconImage())
                    .color(goal.getIcon().getName())
                    .build();
        }).toList();
    }

    // 특정 목표(goalId) 삭제
    public void deleteGoalById(Long userId, Long goalId) {
        var goal = calendarRepository.findById(goalId).orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        if (!goal.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to delete this goal");
        }
        calendarRepository.delete(goal);
    }
}