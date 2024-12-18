package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.User;

public class GoalMapper {
    public static Goal toEntity(User user, SetGoalRequestDTO goalDTO, Icon icon, Long goalId) {
        return Goal.builder()
                .id(goalId)
                .user(user)
                .name(goalDTO.getGoalName())
                .category(String.valueOf(goalDTO.getGoalType()))
                .amount(Long.valueOf(goalDTO.getGoalMoney()))
                .startAt(goalDTO.getStartDate().atStartOfDay())
                .endAt(goalDTO.getEndDate().atStartOfDay())
                .icon(icon)
                .customImage(goalDTO.getGoalImg())
                .build();
    }

    public static SetGoalResponseDTO toSetGoalResponse(Goal goal) {
        return SetGoalResponseDTO.builder()
                .goalName(goal.getName())
                .goalMoney(goal.getAmount().intValue())
                .startDate(goal.getStartAt().toLocalDate())
                .endDate(goal.getEndAt().toLocalDate())
                .goalType(Integer.parseInt(goal.getCategory()))
                .iconId(goal.getIcon() != null ? goal.getIcon().getId() : null)
                .goalImg(goal.getCustomImage())
                .build();
    }
}
