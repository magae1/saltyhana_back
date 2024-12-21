package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalDTO;
import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.User;

import static com.saltyhana.saltyhanaserver.util.GoalUtil.calculatePercentage;

//public class GoalMapper {
//    public static Goal toEntity(User user, Account account, SetGoalDTO goalDTO, Icon icon, Long goalId) {
//        return Goal.builder()
//                .id(goalId)
//                .user(user)
//                .account(account)
//                .name(goalDTO.getGoalName())
//                .category(String.valueOf(goalDTO.getGoalType()))
//                .amount(Long.valueOf(goalDTO.getGoalMoney()))
//                .startAt(goalDTO.getStartDate().atStartOfDay())
//                .endAt(goalDTO.getEndDate().atStartOfDay())
//                .icon(icon)
//                .customImage(goalDTO.getGoalImg())
//                .build();
//    }
//
//    public static SetGoalDTO toSetGoalDTO(Goal goal) {
//        return SetGoalDTO.builder()
//                .goalName(goal.getName())
//                .goalMoney(goal.getAmount().intValue())
//                .startDate(goal.getStartAt().toLocalDate())
//                .endDate(goal.getEndAt().toLocalDate())
//                .goalType(Integer.parseInt(goal.getCategory()))
//                .iconId(goal.getIcon() != null ? goal.getIcon().getId() : null)
//                .goalImg(goal.getCustomImage())
//                .connectedAccount(goal.getAccount().getId())
//                .build();
//    }
//
//    public static GoalResponseDTO toGoalResponseDTO(Goal goal, User user) {
//        return GoalResponseDTO.builder()
//                .id(goal.getId())
//                .userName(user.getName())
//                .title(goal.getName())
//                .startAt(goal.getStartAt())
//                .endAt(goal.getEndAt())
//                .icon(goal.getIcon())
//                .connected_account(goal.getAccount() != null ? goal.getAccount().getId() : null)
//                .amount(goal.getAmount())
//                .percentage(calculatePercentage(goal))
//                .build();
//    }
//}

public class GoalMapper {
    public static Goal toEntity(User user, Account account, SetGoalDTO goalDTO, Icon icon, Long goalId) {
        return Goal.builder()
                .id(goalId)
                .user(user)
                .account(account)
                .name(goalDTO.getGoalName())
                .category(String.valueOf(goalDTO.getGoalType()))
                .amount(Long.valueOf(goalDTO.getGoalMoney()))
                .startAt(goalDTO.getStartDate().atStartOfDay())
                .endAt(goalDTO.getEndDate().atStartOfDay())
                .icon(icon)
                .customImage(goalDTO.getGoalImg())
                .build();
    }

    public static SetGoalDTO toSetGoalDTO(Goal goal) {
        return SetGoalDTO.builder()
                .goalName(goal.getName())
                .goalMoney(goal.getAmount().intValue())
                .startDate(goal.getStartAt().toLocalDate())
                .endDate(goal.getEndAt().toLocalDate())
                .goalType(Integer.parseInt(goal.getCategory()))
                .iconId(goal.getIcon() != null ? goal.getIcon().getId() : null)
                .goalImg(goal.getCustomImage())
                .connectedAccount(goal.getAccount().getId())
                .build();
    }

    public static GoalResponseDTO toGoalResponseDTO(Goal goal, User user) {
        return GoalResponseDTO.builder()
                .id(goal.getId())
                .userName(user.getName())
                .title(goal.getName())
                .startAt(goal.getStartAt())
                .endAt(goal.getEndAt())
                .iconImage(goal.getIcon() != null ? goal.getIcon().getIconImage() : null)
                .iconColor(goal.getIcon() != null ? goal.getIcon().getColor() : null)
                .customImage(goal.getCustomImage())
                .connected_account(goal.getAccount() != null ? goal.getAccount().getId() : null)
                .amount(goal.getAmount())
                .percentage(calculatePercentage(goal))
                .build();
    }
}