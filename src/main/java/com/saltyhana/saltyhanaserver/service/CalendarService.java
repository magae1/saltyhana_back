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

}
